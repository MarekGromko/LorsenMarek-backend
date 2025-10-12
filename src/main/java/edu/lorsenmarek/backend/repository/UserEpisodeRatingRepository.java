package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.converter.jdbc.UserEpisodeRatingRowMapper;
import edu.lorsenmarek.backend.model.UserEpisodeRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserEpisodeRatingRepository {
    @Autowired
    private JdbcTemplate jdbc;
    final private UserEpisodeRatingRowMapper mapper;
    UserEpisodeRatingRepository() {
        this.mapper = new UserEpisodeRatingRowMapper();
    }
    public List<UserEpisodeRating> findByUserId (Long userId) {
        return jdbc.query("SELECT * FROM user_episode_rating WHERE user_id = ?", mapper, userId);
    }
    public List<UserEpisodeRating> findByEpisodeId (Long episodeId) {
        return jdbc.query("SELECT * FROM user_episode_rating WHERE episode_id = ?", mapper, episodeId);
    }
    public Optional<UserEpisodeRating> findByUserIdAndEpisodeId(Long userId, Long episodeId) {
        return jdbc.query("SELECT * FROM user_episode_rating WHERE user_id = ? AND episode_id = ?", mapper, userId, episodeId).stream().findFirst();
    }
    public void insert(UserEpisodeRating data) {
        jdbc.update("""
                INSERT INTO user_episode_rating
                (user_id, episode_id, created_at, modified_at, rating)
                VALUES (?,?,?,?,?)""",
                data.getUserId(),
                data.getEpisodeId(),
                data.getCreatedAt(),
                data.getModifiedAt(),
                data.getRating()
        );
    }
    public int update(UserEpisodeRating data) {
        return jdbc.update("""
                       UPDATE user_episode_rating
                       SET created_at = ?, modified_at = ?, rating = ?
                       WHERE user_id = ? AND episode_id = ?""",
                data.getCreatedAt(),
                data.getModifiedAt(),
                data.getRating(),
                data.getUserId(),
                data.getEpisodeId()
        );
    }
    public int deleteByUserIdAndEpisodeId(Long userId, Long episodeId) {
        return jdbc.update("""
                DELETE FROM user_episode_rating
                WHERE user_id = ? AND episode_id = ?
                """,
                userId,
                episodeId
        );
    }
}
