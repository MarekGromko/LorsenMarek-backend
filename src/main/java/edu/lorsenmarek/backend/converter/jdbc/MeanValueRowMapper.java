package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.common.MeanValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MeanValueRowMapper implements RowMapper<MeanValue> {
    final private Object sumIndex;
    final private Object countIndex;
    public MeanValueRowMapper(Integer sumIndex, Integer countIndex) {
        this.sumIndex = sumIndex;
        this.countIndex = countIndex;
    }
    public MeanValueRowMapper(String sumIndex, String countIndex) {
        this.sumIndex = sumIndex;
        this.countIndex = countIndex;
    }
    public MeanValueRowMapper() {
        this.sumIndex = 0;
        this.countIndex = 1;
    }
    @Override
    @NonNull
    public MeanValue mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new MeanValue(
                sumIndex instanceof String ? rs.getDouble((String)sumIndex) : rs.getDouble((Integer)sumIndex),
                countIndex instanceof String ? rs.getDouble((String)countIndex) : rs.getDouble((Integer)countIndex)
        );
    }
}
