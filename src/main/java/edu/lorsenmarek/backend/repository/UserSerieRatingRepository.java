package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.converter.jdbc.UserSerieRatingRowMapper;
import edu.lorsenmarek.backend.model.UserSerieRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public class UserSerieRatingRepository {
    final private JdbcTemplate jdbc;
    final private RowMapper<UserSerieRating> mapper;
    UserSerieRatingRepository(final JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new UserSerieRatingRowMapper();
    }
    public List<UserSerieRating> findByUserId (Long userId) {
        return jdbc.query("SELECT * FROM user_serie_rating WHERE user_id = ?", mapper, userId);
    }
    public List<UserSerieRating> findBySerieId (Long serieId) {
        return jdbc.query("SELECT * FROM user_serie_rating WHERE serie_id = ?", mapper, serieId);
    }
    public Optional<UserSerieRating> findByUserIdAndSerieId(Long userId, Long serieId) {
        return jdbc.query("SELECT * FROM user_serie_rating WHERE user_id = ? AND serie_id", mapper, userId, serieId).stream().findFirst();
    }
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
