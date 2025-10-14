package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.common.MeanValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of {@link RowMapper} for {@link MeanValue}
 *
 * @author Marek Gromko
 */
public class MeanValueRowMapper implements RowMapper<MeanValue> {
    final private Object sumIndex;
    final private Object countIndex;

    /**
     * Create a row mapper with specified column indexes
     *
     * @param sumIndex the index for the column with the sum
     * @param countIndex the index for the column with the count
     */
    public MeanValueRowMapper(Integer sumIndex, Integer countIndex) {
        this.sumIndex = sumIndex;
        this.countIndex = countIndex;
    }
    /**
     * Create a row mapper with specified column names
     *
     * @param sumName the name for the column with the sum
     * @param countName the name for the column with the count
     */
    public MeanValueRowMapper(String sumName, String countName) {
        this.sumIndex = sumName;
        this.countIndex = countName;
    }
    /**
     * Create a row mapper with default index
     * <p>the sum column is by default at index 0</p>
     * <p>the count column is by default at index 1</p>
     */
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
