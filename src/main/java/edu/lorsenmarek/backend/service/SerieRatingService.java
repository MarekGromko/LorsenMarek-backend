package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.converter.jdbc.MeanValueRowMapper;
import edu.lorsenmarek.backend.exception.*;
import edu.lorsenmarek.backend.model.UserSerieRating;
import edu.lorsenmarek.backend.repository.SerieRepository;
import edu.lorsenmarek.backend.repository.UserSerieRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static edu.lorsenmarek.backend.repository.UserSerieRatingRepository.Ids;
import java.time.Instant;

/**
 * Service for managing rating of {@link edu.lorsenmarek.backend.model.Serie}BY USERS.
 * @author Lorsen Lamour
 * @version 1.0
 */
@Service
public class SerieRatingService {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private UserMediaHistoryService userMediaHistoryService;
    @Autowired
    private UserSerieRatingRepository userSerieRatingRepo;
    @Autowired
    private SerieRepository serieRepo;

    /**
     * Create or updates a rating for a given series by user.
     * <p>If the user has already rated the series the raring is updated and the modification timestamp is updated</p>
     * @param userId the ID of the user creating the rating
     * @param serieId the ID of the series being rated
     * @param rating the rating value
     * @throws RatingUnwatchedMediaException if the user has not watched the series
     */
    public void tryRating(Long userId, Long serieId, Integer rating) throws RatingUnwatchedMediaException {
        if(!serieRepo.existsById(serieId))
            throw new ResourceNotFoundException("serie", rating.toString());
        if(!userMediaHistoryService.hasWatchedSerie(userId, serieId))
            throw new RatingUnwatchedMediaException();

        userSerieRatingRepo.findOneByIds(new Ids(userId, serieId)).ifPresentOrElse(
                present -> {
                    present.setModifiedAt(Instant.now());
                    present.setRating(rating);
                    userSerieRatingRepo.update(present);
                },
                () -> {
                    var present = UserSerieRating.builder()
                            .createdAt(Instant.now())
                            .modifiedAt(null)
                            .serieId(serieId)
                            .userId(userId)
                            .rating(rating)
                            .build();
                    userSerieRatingRepo.insert(present);
                }
        );
    }

    /**
     * Deletes the rating of a series by a user.
     * @param userId the ID of the user
     * @param serieId the ID of the series.
     */
    public void deleteRating(Long userId, Long serieId) {
        userSerieRatingRepo.deleteByIds(new Ids(userId, serieId));
    }

    /**
     * Calculates the mean rating for a given series.
     * <p>Retrieves the sum and count of rating form database
     * and return a {@link MeanValue} object containing these values</p>
     * @param serieId the ID of the series
     * @return a {@link MeanValue} containing the total sum of ratings and the count
     * @throws ResourceNotFoundException if no rating exist for the given series
     */
    public MeanValue getMeanRating(Long serieId) {
        if(userSerieRatingRepo.findByIds(new Ids(null, serieId)).isEmpty())
            throw new ResourceNotFoundException("serie", serieId.toString());

        return jdbc.query("""
                SELECT SUM(rating), COUNT(*)
                FROM user_serie_rating
                WHERE serie_id = ?
                """,
                new MeanValueRowMapper(0, 1),
                serieId
        ).stream().findFirst().orElse(new MeanValue(0,0));
    }
}
