package edu.lorsenmarek.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for tracking which media series or episode a user has watched.
 * @author Lorsen Lamour
 * @version 1.0
 */
@Service
public class UserMediaHistoryService {
    @Autowired
    private JdbcTemplate jdbc;

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
                    FROM
                        user_episode_history AS history
                        INNER JOIN episode ON history.episode_id = episode.id
                        INNER JOIN season ON episode.season_id = season.id
                        INNER JOIN serie ON season.serie_id = serie.id
                    WHERE
                        history.user_id = ? AND
                        serie.id = ?
                ) AS "exists"
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
                        serie_id = ?
                ) AS "exists"
                """,
                Boolean.class,
                userId,
                episodeId
        );
    }
}
