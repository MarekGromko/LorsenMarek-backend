package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.Person;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class PersonRepository {
    private final JdbcTemplate jdbc;
    private final PersonMapper personMapper;
    PersonRepository(final JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.personMapper = new PersonMapper();
    }
   public  Optional<Person> findById(int id) {
        try {
            return jdbc.query("SELECT * FROM person WHERE id = ?", personMapper, id).stream().findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Person> findAll() {
        try {
            return jdbc.query("Select * from person", personMapper);
        }catch (DataAccessException e){
            return new ArrayList<>();
        }
    }

    public boolean existsById(int id){
        try{
            Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM person WHERE id = ?", Integer.class,id);
            return count != null && count >0;

        }catch (DataAccessException e) {
            return false;
        }
    }
    public void save(Person person){
        try {
            jdbc.update(
                    "INSERT INTO person (id, email, gender, first_name, last_name) VALUES (?, ?, ?, ?, ?)",
                    person.getId(),
                    person.getEmail(),
                    person.getGender(),
                    person.getFirstName(),
                    person.getLastName()
            );

        } catch (DataAccessException e) {
            System.err.println("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    public void deleteById(int id){
        try {
            jdbc.update("DELETE FROM person WHERE id=?",id);
        }catch (DataAccessException e){
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
    public List<Person> searchByName(String name) {
        try {
            return jdbc.query(
                    "SELECT * FROM person WHERE last_name = ?",
                    personMapper,
                    name
            );
        } catch (DataAccessException e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
            return Collections.emptyList();

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
