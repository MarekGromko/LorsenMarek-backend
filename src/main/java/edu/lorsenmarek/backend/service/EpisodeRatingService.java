package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.converter.jdbc.MeanValueRowMapper;
import edu.lorsenmarek.backend.exception.RatingUnwatchedMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.UserEpisodeRating;
import edu.lorsenmarek.backend.repository.EpisodeRepository;
import edu.lorsenmarek.backend.repository.UserEpisodeRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EpisodeRatingService {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private UserMediaHistoryService userMediaHistoryService;
    @Autowired
    private UserEpisodeRatingRepository userEpisodeRatingRepo;
    @Autowired
    private EpisodeRepository episodeRepo;
    public void tryRating(Long userId, Long episodeId, Integer rating) {
        if(!episodeRepo.existsById(episodeId))
            throw new ResourceNotFoundException("episode", rating.toString());
        if(!userMediaHistoryService.hasWatchedEpisode(userId, episodeId))
            throw new RatingUnwatchedMediaException();

        userEpisodeRatingRepo.findByUserIdAndEpisodeId(userId, userId).ifPresentOrElse(
                present-> {
                    present.setModifiedAt(Instant.now());
                    present.setRating(rating);
                    userEpisodeRatingRepo.update(present);
                },
                () -> {
                    var present = UserEpisodeRating.builder()
                            .createdAt(Instant.now())
                            .modifiedAt(null)
                            .episodeId(episodeId)
                            .userId(userId)
                            .rating(rating)
                            .build();
                    userEpisodeRatingRepo.insert(present);
                }
        );
    }
    public void deleteRating(Long userId, Long episodeId) {
        userEpisodeRatingRepo.deleteByUserIdAndEpisodeId(userId, episodeId);
    }
    public MeanValue getMeanRating(Long episodeId) {
        if(userEpisodeRatingRepo.findByEpisodeId(episodeId).isEmpty())
            throw new ResourceNotFoundException("episode", episodeId.toString());
        return jdbc.query("""
                SELECT SUM(RATING), COUNT(*)
                FROM user_episode_rating
                WHERE episode_id = ?
                """,
                new MeanValueRowMapper(),
                episodeId
        ).stream().findFirst().orElse(new MeanValue(0,0));
    }
}
