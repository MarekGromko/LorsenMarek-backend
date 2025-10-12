package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.model.UserEpisodeRating;
import edu.lorsenmarek.backend.model.UserSerieRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEpisodeRatingRowMapper implements RowMapper<UserEpisodeRating> {
    @Override
    @NonNull
    public UserEpisodeRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEpisodeRating.builder()
                .userId(rs.getLong("user_id"))
                .episodeId(rs.getLong("episode_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .modifiedAt(rs.getTimestamp("modified_at").toInstant())
                .rating(rs.getInt("rating"))
                .build();
    }
}
