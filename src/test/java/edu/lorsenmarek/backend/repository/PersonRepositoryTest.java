package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.Person;
import edu.lorsenmarek.backend.common.PageOptions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonRepositoryTest {

    final Person JaneDoe = Person.builder()
            .id(5)
            .firstName("Jane")
            .lastName("Doe")
            .email("janeDoe@abc.def")
            .gender("F")
            .build();
    @Mock
    NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    PersonRepository personRepo;

    @Test
    void findById_checkValidSQL() {
        // arrange
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<Person>>any())).thenReturn(List.of(new Person()));

        // act
        personRepo.findById(10);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<Person>>any());

        // assert
        assertEquals("SELECT * FROM person WHERE id = :id", sqlCaptor.getValue());
        assertEquals(10, paramCaptor.getValue().getValue("id"));
    }
    @Nested
    class findAll{
        @Test
        void whenNoPage_SQLShouldSelectAll() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<Person>>any())).thenReturn(List.of(new Person()));

            // act
            personRepo.findAll(null);

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<Person>>any());

            // assert
            assertEquals("SELECT * FROM person ORDER BY id", sqlCaptor.getValue());
        }
        @Test
        void whenPage_SQLShouldSelectWithLimitOffset() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<Person>>any())).thenReturn(List.of(new Person()));

            // act
            personRepo.findAll(new PageOptions(5, 10));

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<Person>>any());

            // assert
            assertEquals("SELECT * FROM person ORDER BY id LIMIT :limit OFFSET :offset", sqlCaptor.getValue());
            assertEquals(5+1, paramCaptor.getValue().getValue("limit"));
            assertEquals(5*10, paramCaptor.getValue().getValue("offset"));
        }
    }
    @Test
    void existsById_checkValidSQL() {
        // arrange
        when(jdbcTemplate.queryForList(anyString(), any(SqlParameterSource.class))).thenReturn(List.of(new HashMap<>()));

        // act
        personRepo.existsById(10);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).queryForList(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("SELECT id FROM person WHERE id = ? LIMIT 1", sqlCaptor.getValue());
        assertEquals(10, paramCaptor.getValue().getValue("id"));
    }
    @Test
    void update_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        personRepo.update(JaneDoe);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("""
                    UPDATE person SET
                        email = :email,
                        gender = :gender,
                        first_name = :first_name,
                        last_name = :last_name
                    WHERE id = :id
                    """, sqlCaptor.getValue());
        assertEquals("Jane", paramCaptor.getValue().getValue("first_name"));

    }

    @Test
    void insert_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        personRepo.insert(JaneDoe);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("INSERT INTO person (email, gender, first_name, last_name) VALUES (:email, :gender, :first_name, :last_name)\n", sqlCaptor.getValue());
        assertEquals("Doe", paramCaptor.getValue().getValue("last_name"));
    }

    @Test
    void deleteById_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        personRepo.deleteById(10);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("DELETE FROM person WHERE id = :id", sqlCaptor.getValue());
        assertEquals(10, paramCaptor.getValue().getValue("id"));
    }
    @Nested
    class searchByName{
        @Test
        void whenNoPage_SQLShouldSelectINSTR() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<Person>>any())).thenReturn(List.of(new Person()));

            // act
            personRepo.searchByName("abc",  null);

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<Person>>any());

            // assert
            assertEquals("SELECT * FROM person WHERE INSTR(CONCAT(first_name, ' ', last_name), :hint) > 0 ORDER BY id", sqlCaptor.getValue());
            assertEquals("abc", paramCaptor.getValue().getValue("hint"));
        }
        @Test
        void whenPage_SQLShouldAddLimitAndOffset() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<Person>>any())).thenReturn(List.of(new Person()));

            // act
            personRepo.searchByName("abc",  new PageOptions(10, 1));

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<Person>>any());

            // assert
            assertEquals("SELECT * FROM person WHERE INSTR(CONCAT(first_name, ' ', last_name), :hint) > 0 ORDER BY id LIMIT :limit OFFSET :offset", sqlCaptor.getValue());
            assertEquals("abc", paramCaptor.getValue().getValue("hint"));
            assertEquals(11, paramCaptor.getValue().getValue("limit"));
            assertEquals(0, paramCaptor.getValue().getValue("offset"));
        }
    }
}