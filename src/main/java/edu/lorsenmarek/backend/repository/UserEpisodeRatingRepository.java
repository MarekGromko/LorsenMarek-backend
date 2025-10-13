package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.converter.jdbc.UserEpisodeRatingRowMapper;
import edu.lorsenmarek.backend.model.UserEpisodeRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *  Repository to access {@link UserEpisodeRating} table
 *
 * @author Marek Gromko
 */
@Repository
public class UserEpisodeRatingRepository {
    @Autowired
    private JdbcTemplate jdbc;
    final private UserEpisodeRatingRowMapper mapper;
    UserEpisodeRatingRepository() {
        this.mapper = new UserEpisodeRatingRowMapper();
    }

    /**
     * Find all {@link UserEpisodeRating} from their user id
     *
     * @param userId the user id to search on
     * @return a {@link List} of {@link UserEpisodeRating} given a user id
     */
    public List<UserEpisodeRating> findByUserId (Long userId) {
        return jdbc.query("SELECT * FROM user_episode_rating WHERE user_id = ?", mapper, userId);
    }
    /**
     * Find all {@link UserEpisodeRating} from its episode id
     *
     * @param episodeId the episode id to search on
     * @return a {@link List} of {@link UserEpisodeRating} give an episode id
     */
    public List<UserEpisodeRating> findByEpisodeId (Long episodeId) {
        return jdbc.query("SELECT * FROM user_episode_rating WHERE episode_id = ?", mapper, episodeId);
    }
    /**
     * Find {@link UserEpisodeRating} from its episode & user id
     *
     * @param userId the user id to search on
     * @param episodeId the episode id to search on
     * @return an {@link Optional} of {@link UserEpisodeRating} give its primary ids
     */
    public Optional<UserEpisodeRating> findByUserIdAndEpisodeId(Long userId, Long episodeId) {
        return jdbc.query("SELECT * FROM user_episode_rating WHERE user_id = ? AND episode_id = ?", mapper, userId, episodeId).stream().findFirst();
    }
    /**
     * Insert a new {@link UserEpisodeRating}
     *
     * @param data the {@link UserEpisodeRating} to insert
     */
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
    /**
     * Update a new {@link UserEpisodeRating} given a user id & episode id
     *
     * @param data the {@link UserEpisodeRating} to update
     * @return the number of row updated (1 or 0)
     */
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

    /**
     * Delete a {@link UserEpisodeRating} given a user id & episode id
     *
     * @param userId the user id to search on
     * @param episodeId the episode id to search on
     * @return the number of row deleted (1 or 0)
     */
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
