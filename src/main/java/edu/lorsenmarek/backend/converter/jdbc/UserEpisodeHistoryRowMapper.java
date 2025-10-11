package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.model.UserEpisodeHistory;
import edu.lorsenmarek.backend.model.UserSerieRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEpisodeHistoryRowMapper implements RowMapper<UserEpisodeHistory> {
    @Override
    @NonNull
    public UserEpisodeHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEpisodeHistory.builder()
                .userId(rs.getLong("user_id"))
                .episodeId(rs.getLong("serie_id"))
                .watchedAt(rs.getTimestamp("created_at").toInstant())
                .watchedDuration(rs.getInt("modified_at"))
                .build();
    }
}
