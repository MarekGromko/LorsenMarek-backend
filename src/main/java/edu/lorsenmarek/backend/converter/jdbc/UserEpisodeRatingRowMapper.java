package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.model.UserEpisodeRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Implementation of {@link RowMapper} for {@link UserEpisodeRating}
 *
 * @author Marek Gromko
 */
public class UserEpisodeRatingRowMapper implements RowMapper<UserEpisodeRating> {
    /** Create a new {@link UserEpisodeRatingRowMapper} */
    public UserEpisodeRatingRowMapper() {}
    @Override
    @NonNull
    public UserEpisodeRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEpisodeRating.builder()
                .userId(rs.getLong("user_id"))
                .episodeId(rs.getLong("episode_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .modifiedAt(Optional
                        .ofNullable(rs.getTimestamp("modified_at"))
                        .map(Timestamp::toInstant)
                        .orElse(null))
                .rating(rs.getInt("rating"))
                .build();
    }
}
