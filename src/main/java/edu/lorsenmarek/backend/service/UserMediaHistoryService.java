package edu.lorsenmarek.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for tracking which media series or episode a user has watched.
 *  @author Marek Gromko
 * @version 1.0
 */
@Service
public class UserMediaHistoryService {
    final private JdbcTemplate jdbc;
    /**
     * Create a new {@link UserMediaHistoryService}
     * @param jdbc depends on {@link JdbcTemplate}
     */
    public UserMediaHistoryService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    /**
     * Checks if a user has watched a given series.
     * @param userId the ID of the user.
     * @param serieId the series ID
     * @return {@code true} if the user has watched at least one episode of the series {@code false} otherwise.
     */
    public boolean hasWatchedSerie(Long userId, Long serieId) {
        return jdbc.queryForObject("""
                SELECT EXISTS (
                    SELECT 1
                    FROM user_serie_history
                    WHERE
                        user_id = ? AND
                        serie_id = ?
                ) AS exists_flag
                """,
                Boolean.class,
                userId,
                serieId
        );
    }

    /**
     * Checks if a user has watched a specific episode
     * @param userId the id of the user
     * @param episodeId the ID of the episode
     * @return {@code true} if the user has watched the episode {@code false} otherwise.
     */
    public boolean hasWatchedEpisode(Long userId, Long episodeId) {
        return jdbc.queryForObject("""
                SELECT EXISTS (
                    SELECT 1
                    FROM user_episode_history
                    WHERE
                        user_id = ? AND
                        episode_id = ?
                ) AS exists_flag
                """,
                Boolean.class,
                userId,
                episodeId
        );
    }
}
