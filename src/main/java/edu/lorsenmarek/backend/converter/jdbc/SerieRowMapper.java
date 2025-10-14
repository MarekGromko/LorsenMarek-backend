package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.model.Serie;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SerieRowMapper implements RowMapper<Serie> {
    @Override
    @NonNull
    public Serie mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Serie.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .releasedAt(rs.getTimestamp("released_at").toInstant())
                .build();
    }
}
