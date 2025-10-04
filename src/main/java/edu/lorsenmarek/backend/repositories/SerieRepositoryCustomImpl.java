package edu.lorsenmarek.backend.repositories;

import edu.lorsenmarek.backend.commons.SerieSearchOption;
import edu.lorsenmarek.backend.models.Serie;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SerieRepositoryCustomImpl implements SerieRepositoryCustom{
    final private NamedParameterJdbcTemplate jdbc;
    final private SerieMapper mapper = new SerieMapper();
    SerieRepositoryCustomImpl(final NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    @Override
    public List<Serie> searchByOption(SerieSearchOption opt) {
        var sql    = new StringBuilder().append("SELECT * FROM serie WHERE 1=1");
        var params = new MapSqlParameterSource();

        if(opt.getTitle() != null && !opt.getTitle().isEmpty()) {
            sql.append(" AND INSTR(title, :title) > 0");
            params.addValue("title", opt);
        }

        return jdbc.query(sql.toString(), params, mapper);
    }
    public static class SerieMapper implements RowMapper<Serie> {
        @Override
        @NonNull
        public Serie mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Serie.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .build();
        }
    }
}
