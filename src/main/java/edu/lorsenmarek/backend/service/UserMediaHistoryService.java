package edu.lorsenmarek.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserMediaHistoryService {
    @Autowired
    private JdbcTemplate jdbc;
    public boolean hasWatchedSerie(Long userId, Long serieId) {
        return jdbc.queryForObject("""
                SELECT EXISTS (
                    SELECT 1
                    FROM user_serie_history
                    WHERE
                        user_id = ? AND
                        serie_id = ?
                    LIMIT 1
                ) AS "exists"
                """,
                Boolean.class,
                userId,
                serieId
        );
    }
    public boolean hasWatchedEpisode(Long userId, Long episodeId) {
        return jdbc.queryForObject("""
                SELECT EXISTS (
                    SELECT 1
                    FROM user_episode_history
                    WHERE
                        user_id = ? AND
                        serie_id = ?
                    LIMIT 1
                ) AS "exists"
                """,
                Boolean.class,
                userId,
                episodeId
        );
    }
}
