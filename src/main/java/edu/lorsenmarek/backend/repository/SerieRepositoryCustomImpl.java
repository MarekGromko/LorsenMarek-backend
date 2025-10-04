package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.common.option.SerieSearchOptions;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.converter.jdbc.SerieRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SerieRepositoryCustomImpl implements SerieRepositoryCustom{
    final private NamedParameterJdbcTemplate jdbc;
    final private SerieRowMapper mapper = new SerieRowMapper();
    SerieRepositoryCustomImpl(final NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    @Override
    public List<Serie> searchByOptions(SerieSearchOptions opt) {
        var sql    = new StringBuilder().append("SELECT * FROM serie WHERE 1=1");
        var params = new MapSqlParameterSource();

        if(opt.getTitle() != null && !opt.getTitle().isEmpty()) {
            sql.append(" AND INSTR(title, :title) > 0");
            params.addValue("title", opt);
        }

        return jdbc.query(sql.toString(), params, mapper);
    }
}
