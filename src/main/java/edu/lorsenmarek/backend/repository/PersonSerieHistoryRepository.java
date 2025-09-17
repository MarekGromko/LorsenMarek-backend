package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.PersonSerieHistory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonSerieHistoryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PersonSerieHistoryMapper PSHMapper;
    public PersonSerieHistoryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.PSHMapper = new PersonSerieHistoryMapper();
    }
    public List<PersonSerieHistory> findByPerson(int id) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM person_serie_history WHERE person_id = :id",
                    new MapSqlParameterSource().addValue("id", id),
                    PSHMapper);
        } catch(Exception e) {
            return List.of();
        }
    }
    public List<PersonSerieHistory> findBySerie(int id) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM person_serie_history WHERE serie_id = :id",
                    new MapSqlParameterSource().addValue("id", id),
                    PSHMapper);
        } catch(Exception e) {
            return List.of();
        }
    }
    public boolean existsByIds(int personId, int serieId) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM person_serie_history WHERE serie_id = :serie_id AND person_id = :person_id LIMIT 1",
                    new MapSqlParameterSource()
                            .addValue("person_id", personId)
                            .addValue("serie_id", serieId),
                    PSHMapper).size() > 0;
        } catch(Exception e){
            return false;
        }
    }
    public void save(PersonSerieHistory psh) {
        if(existsByIds(psh.getPersonId(), psh.getSerieId())) {
            update(psh);
        } else {
            insert(psh);
        }
    }
    public int update(PersonSerieHistory psh) {
        try {
            return jdbcTemplate.update("""
                    UPDATE person_serie_history SET
                        last_watch = :last_watch
                        instance_watch = :instance_watch
                    WHERE
                        person_id = :person_id AND
                        serie_id = :serie_id""",
                    new MapSqlParameterSource()
                            .addValue("last_watch", psh.getLastWatch())
                            .addValue("instance_watch", psh.getInstanceWatch())
                            .addValue("person_id", psh.getPersonId())
                            .addValue("serie_id", psh.getSerieId())
                    );
        } catch (Exception e) {
            return 0;
        }
    }
    public int insert(PersonSerieHistory psh) {
        try {
            return jdbcTemplate.update("""
                    INSERT INTO person_serie_history (person_id, serie_id, last_watch, instance_watch) VALUES
                    (:person_id, :serie_id, :last_watch, :instance_watch)""",
                    new MapSqlParameterSource()
                            .addValue("last_watch", psh.getLastWatch())
                            .addValue("instance_watch", psh.getInstanceWatch())
                            .addValue("person_id", psh.getPersonId())
                            .addValue("serie_id", psh.getSerieId())
            );
        } catch (Exception e) {
            return 0;
        }
    }
    public int deleteByIds(int personId, int serieId) {
        try {
            return jdbcTemplate.update("""
                    DELETE FROM person_serie_history WHERE person_id = :person_id AND serie_id = :serie_id""",
                    new MapSqlParameterSource()
                            .addValue("person_id", personId)
                            .addValue("serie_id", serieId)
            );
        } catch (Exception e) {
            return 0;
        }
    }
    public static class PersonSerieHistoryMapper implements RowMapper<PersonSerieHistory> {
        @Override
        @NonNull
        public PersonSerieHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PersonSerieHistory.builder()
                    .personId(rs.getInt("person_id"))
                    .serieId(rs.getInt("serie_id"))
                    .lastWatch(rs.getTimestamp("last_watch").toInstant())
                    .instanceWatch(rs.getInt("instance_watch"))
                    .build();
        }
    }
}
