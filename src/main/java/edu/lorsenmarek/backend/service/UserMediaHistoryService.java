package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.converter.jdbc.UserEpisodeHistoryRowMapper;
import edu.lorsenmarek.backend.model.UserEpisodeHistory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import static java.sql.Types.*;

import java.util.Arrays;
import java.util.List;

@Service
public class UserMediaHistoryService {
    final private JdbcTemplate jdbc;
    //final private RowMapper<UserEpisodeHistory> mapper;
    UserMediaHistoryService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        //this.mapper = new UserEpisodeHistoryRowMapper();
    }
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
