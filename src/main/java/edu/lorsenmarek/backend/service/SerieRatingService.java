package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.exception.*;
import edu.lorsenmarek.backend.model.UserSerieRating;
import edu.lorsenmarek.backend.repository.UserSerieRatingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SerieRatingService {
    final private JdbcTemplate jdbc;
    final private UserMediaHistoryService userMediaHistoryService;
    final private UserSerieRatingRepository userSerieRatingRepository;
    SerieRatingService(final JdbcTemplate jdbc, final UserMediaHistoryService userMediaHistoryService, final UserSerieRatingRepository userSerieRatingRepository) {
        this.jdbc = jdbc;
        this.userMediaHistoryService = userMediaHistoryService;
        this.userSerieRatingRepository = userSerieRatingRepository;
    }
    public void tryRating(Long userId, Long serieId, Integer rating) throws RatingUnseenMediaException {
        if(!userMediaHistoryService.hasWatchedSerie(userId, serieId)) {
            throw new RatingUnseenMediaException("User has not watched serie %d".formatted(serieId));
        }

        userSerieRatingRepository.findByUserIdAndSerieId(userId, serieId).ifPresentOrElse(
                present -> {
                    present.setModifiedAt(Instant.now());
                    present.setRating(rating);
                    userSerieRatingRepository.update(present);
                },
                () -> {
                    var present = UserSerieRating.builder()
                            .createdAt(Instant.now())
                            .modifiedAt(null)
                            .serieId(serieId)
                            .userId(userId)
                            .rating(rating)
                            .build();
                    userSerieRatingRepository.insert(present);
                }
        );
    }
    public void deleteRating(Long userId, Long serieId) {
        userSerieRatingRepository.deleteByUserIdAndSerieId(userId, serieId);
    }
    public Integer getAvgRating(Long serieId) {
        return jdbc.queryForObject("""
                SELECT AVG(rating)
                FROM user_serie_rating
                WHERE serie_id = ?
                """,
                Integer.class,
                serieId
        );
    }
}
