package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.model.UserSerieRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Implementation of {@link RowMapper} for {@link UserSerieRating}
 *
 * @author Marek Gromko
 */
public class UserSerieRatingRowMapper implements RowMapper<UserSerieRating> {
    /** Create a new {@link UserSerieRatingRowMapper} */
    public UserSerieRatingRowMapper() {}
    @Override
    @NonNull
    public UserSerieRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserSerieRating.builder()
                .userId(rs.getLong("user_id"))
                .serieId(rs.getLong("serie_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .modifiedAt(Optional
                        .ofNullable(rs.getTimestamp("modified_at"))
                        .map(Timestamp::toInstant)
                        .orElse(null))
                .rating(rs.getInt("rating"))
                .build();
    }
}