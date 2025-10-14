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
    public void deleteRating(Long userId, Long serieId) {
        userSerieRatingRepo.deleteByIds(new Ids(userId, serieId));
    }
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
