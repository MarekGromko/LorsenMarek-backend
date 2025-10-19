package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.converter.jdbc.MeanValueRowMapper;
import edu.lorsenmarek.backend.exception.RatingUnwatchedMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.UserEpisodeRating;
import edu.lorsenmarek.backend.repository.EpisodeRepository;
import edu.lorsenmarek.backend.repository.UserEpisodeRatingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static edu.lorsenmarek.backend.repository.UserEpisodeRatingRepository.*;

// TODO: RatingService should be made abstract

/**
 * Service managing rating functionalities related to {@link UserEpisodeRating}
 *
 * @see UserEpisodeRating
 * @see UserEpisodeRatingRepository
 * @author Marek Gromko
 */
@Service
public class EpisodeRatingService {
    final private JdbcTemplate jdbc;
    final private UserMediaHistoryService userMediaHistoryService;
    final private UserEpisodeRatingRepository userEpisodeRatingRepo;
    final private EpisodeRepository episodeRepo;

    /**
     * Create a new {@link EpisodeRatingService}
     * @param jdbc depends on {@link JdbcTemplate}
     * @param userMediaHistoryService depends on {@link UserMediaHistoryService}
     * @param userEpisodeRatingRepo depends on {@link UserEpisodeRatingRepository}
     * @param episodeRepo depends on {@link EpisodeRepository}
     */
    public EpisodeRatingService(
            JdbcTemplate jdbc,
            UserMediaHistoryService userMediaHistoryService,
            UserEpisodeRatingRepository userEpisodeRatingRepo,
            EpisodeRepository episodeRepo
    ) {
        this.jdbc = jdbc;
        this.userMediaHistoryService = userMediaHistoryService;
        this.userEpisodeRatingRepo = userEpisodeRatingRepo;
        this.episodeRepo = episodeRepo;
    }

    /**
     * Try to rate an episode
     *
     * @param userId the user doing the rating
     * @param episodeId the episode to be rated
     * @param rating the rating
     * @throws ResourceNotFoundException if the episode does not exist
     * @throws RatingUnwatchedMediaException if the user has not yet watch the episode
     */
    public void tryRating(Long userId, Long episodeId, Integer rating) throws ResourceNotFoundException, RatingUnwatchedMediaException {
        if(!episodeRepo.existsById(episodeId))
            throw new ResourceNotFoundException("episode", rating.toString());
        if(!userMediaHistoryService.hasWatchedEpisode(userId, episodeId))
            throw new RatingUnwatchedMediaException();

        userEpisodeRatingRepo.findOneByIds(new Ids(userId, userId)).ifPresentOrElse(
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

    /**
     * Delete a rating
     * <br/>
     * This function does not tell if the rating to delete existed or not
     *
     * @param userId the user id
     * @param episodeId the episode id
     */
    public void deleteRating(Long userId, Long episodeId) {
        userEpisodeRatingRepo.deleteByIds(new Ids(userId, episodeId));
    }

    /**
     * Get the mean (average) rating for a given episode
     *
     * @param episodeId the rating episode id to search on
     * @return {@link MeanValue} the mean value, if there is no rating, {@link MeanValue#count()} will be 0
     * @throws ResourceNotFoundException if episode does not exist
     */
    public MeanValue getMeanRating(Long episodeId) throws ResourceNotFoundException {
        if(!episodeRepo.existsById(episodeId))
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
