package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.converter.jdbc.UserSerieRatingRowMapper;
import edu.lorsenmarek.backend.model.UserSerieRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository to access {@link UserSerieRating} table
 *
 * @author Marek Gromko
 */
@Repository
public class UserSerieRatingRepository {
    @Autowired
    private JdbcTemplate jdbc;
    final private UserSerieRatingRowMapper mapper;
    UserSerieRatingRepository() {
        this.mapper = new UserSerieRatingRowMapper();
    }

    /**
     * Find all {@link UserSerieRating} from their user id
     *
     * @param userId the user id to search on
     * @return a {@link List} of {@link UserSerieRating} given a user id
     */
    public List<UserSerieRating> findByUserId (Long userId) {
        return jdbc.query("SELECT * FROM user_serie_rating WHERE user_id = ?", mapper, userId);
    }

    /**
     * Find all {@link UserSerieRating} from their serie id
     *
     * @param serieId the serie id to search on
     * @return a {@link List} of {@link UserSerieRating} given a serie id
     */
    public List<UserSerieRating> findBySerieId (Long serieId) {
        return jdbc.query("SELECT * FROM user_serie_rating WHERE serie_id = ?", mapper, serieId);
    }
    /**
     * Find {@link UserSerieRating} from its serie & user id
     *
     * @param userId the user id to search on
     * @param serieId the episode id to search on
     * @return an {@link Optional} of {@link UserSerieRating} give its primary ids
     */
    public Optional<UserSerieRating> findByUserIdAndSerieId(Long userId, Long serieId) {
        return jdbc.query("SELECT * FROM user_serie_rating WHERE user_id = ? AND serie_id = ?", mapper, userId, serieId).stream().findFirst();
    }
    /**
     * Insert a new {@link UserSerieRating}
     *
     * @param data the {@link UserSerieRating} to insert
     */
    public void insert(UserSerieRating data) {
       jdbc.update("""
                       INSERT INTO user_serie_rating
                       (user_id, serie_id, created_at, modified_at, rating)
                       VALUES (?,?,?,?,?)""",
               data.getUserId(),
               data.getSerieId(),
               data.getCreatedAt(),
               data.getModifiedAt(),
               data.getRating()
        );
    }
    /**
     * Update a new {@link UserSerieRating} given a user id & serie id
     *
     * @param data the {@link UserSerieRating} to update
     * @return the number of row updated (1 or 0)
     */
    public int update(UserSerieRating data) {
        return jdbc.update("""
                       UPDATE user_serie_rating
                       SET created_at = ?, modified_at = ?, rating = ?
                       WHERE user_id = ? AND serie_id = ?""",
                data.getCreatedAt(),
                data.getModifiedAt(),
                data.getRating(),
                data.getUserId(),
                data.getSerieId()
        );
    }
    /**
     * Delete a {@link UserSerieRating} given a user id & episode id
     *
     * @param userId the user it
     * @param serieId the episode id
     * @return the number of row deleted (1 or 0)
     */
    public int deleteByUserIdAndSerieId(Long userId, Long serieId) {
        return jdbc.update("""
                DELETE FROM user_serie_rating
                WHERE user_id = ? AND serie_id = ?
                """,
                userId,
                serieId
        );
    }
}
