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
