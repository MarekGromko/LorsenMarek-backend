package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.model.UserSerieRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of {@link RowMapper} for {@link UserSerieRating}
 *
 * @author Marek Gromko
 */
public class UserSerieRatingRowMapper implements RowMapper<UserSerieRating> {
    @Override
    @NonNull
    public UserSerieRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserSerieRating.builder()
                .userId(rs.getLong("user_id"))
                .serieId(rs.getLong("serie_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .modifiedAt(rs.getTimestamp("modified_at").toInstant())
                .rating(rs.getInt("rating"))
                .build();
    }
}