package edu.lorsenmarek.backend.repository.mapper;

import edu.lorsenmarek.backend.model.Serie;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SerieMapper implements RowMapper<Serie> {
    @Override
    @NonNull
    public Serie mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Serie.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .build();
    }
}
