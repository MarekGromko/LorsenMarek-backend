package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.Person;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class PersonRepository {
    private final JdbcTemplate jdbc;
    private final PersonMapper personMapper;
    PersonRepository(final JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.personMapper = new PersonMapper();
    }
    Optional<Person> findById(int index) {
        try {
            return jdbc.query("SELECT * FROM person WHERE id = ?", personMapper, index).stream().findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
    private static class PersonMapper implements RowMapper<Person> {
        @Override
        @NonNull
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Person.builder()
                    .id(rs.getInt("id"))
                    .email(rs.getString("email"))
                    .gender(rs.getString("gender"))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .build();
        }
    }
}
