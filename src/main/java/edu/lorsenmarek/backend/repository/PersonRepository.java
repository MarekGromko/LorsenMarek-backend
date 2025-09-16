package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.Person;
import edu.lorsenmarek.backend.utility.PageOptions;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class PersonRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final PersonMapper personMapper;
    PersonRepository(final NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.personMapper = new PersonMapper();
    }

    public Optional<Person> findById(int id) {
        try {
            var params = new MapSqlParameterSource().addValue("id", id);
            return jdbc.query("SELECT * FROM person WHERE id = :id", params,  personMapper).stream().findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
    public List<Person> findAll(PageOptions pageOpts) {
        var sql = "";
        var params = new MapSqlParameterSource();
        if(pageOpts == null) {
            sql = "SELECT * FROM person ORDER BY id";
        } else {
            sql = "SELECT * FROM person ORDER BY id LIMIT :limit OFFSET :offset";
            params.addValue("limit", pageOpts.getPageSize());
            params.addValue("offset", pageOpts.getPageSize() * pageOpts.getPageIndex());
        }
        try {
            return jdbc.query(sql, params, personMapper);
        }catch (DataAccessException e){
            return new ArrayList<>();
        }
    }
    public boolean existsById(int id){
        try{
            var params = new MapSqlParameterSource().addValue("id", id);
            return jdbc.queryForList("SELECT id FROM person WHERE id = ? LIMIT 1", params).size() > 0;
        }catch (DataAccessException e) {
            return false;
        }
    }
    public void save(Person person){
        try {
            if(person.getId() > 0 && existsById(person.getId()))
            {
                update(person);
            }
            else
            {
                insert(person);
            }
        } catch (DataAccessException e) {
            return;
        }
    }
    public int update(Person person) {
        if(person.getId() == null)
            return 0;
        var params = new MapSqlParameterSource()
                .addValue("id", person.getId())
                .addValue("first_name", person.getFirstName())
                .addValue("last_name", person.getLastName())
                .addValue("email", person.getEmail())
                .addValue("gender", person.getGender());
        try {
            return jdbc.update("""
                    UPDATE person SET
                        email = :email,
                        gender = :gender,
                        first_name = :first_name,
                        last_name = :last_name
                    WHERE id = :id
                    """,
                    params
            );
        } catch(Exception e){
            return 0;
        }
    }
    public int insert(Person person) {
        if(person.getId() == null)
            return 0;
        var params = new MapSqlParameterSource()
                .addValue("first_name", person.getFirstName())
                .addValue("last_name", person.getLastName())
                .addValue("email", person.getEmail())
                .addValue("gender", person.getGender());
        try {
            return jdbc.update("""
                        INSERT INTO person (email, gender, first_name, last_name) VALUES (:email, :gender, :first_name, :last_name)
                        """,
                    params
            );
        } catch(Exception e) {
            return 0;
        }
    }
    public int deleteById(int id){
        try {
            var params = new MapSqlParameterSource().addValue("id", id);
            return jdbc.update("DELETE FROM person WHERE id = :id", params);
        }catch (DataAccessException e){
            return 0;
        }
    }
    public List<Person> searchByName(String hint, PageOptions pageOpts) {
        var sql = "SELECT * FROM person WHERE INSTR(CONCAT(first_name, ' ', last_name), :hint) > 0 ORDER BY id";
        var params = new MapSqlParameterSource().addValue("hint", hint);
        if(pageOpts != null) {
            sql += " LIMIT :limit OFFSET :offset";
            params.addValue("limit", 1 + pageOpts.getPageSize());
            params.addValue("offset", pageOpts.getPageSize() * (pageOpts.getPageIndex()-1));
        }
        try {
            return jdbc.query(sql, params, personMapper);
        }catch (DataAccessException e){
            return new ArrayList<>();
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
