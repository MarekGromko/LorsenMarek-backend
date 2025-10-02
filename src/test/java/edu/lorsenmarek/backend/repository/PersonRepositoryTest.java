package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.User;
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
class UserRepositoryTest {

    final User JaneDoe = User.builder()
            .id(5L)
            .firstName("Jane")
            .lastName("Doe")
            .email("janeDoe@abc.def")
            .title("Ms")
            .build();
    @Mock
    NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    UserRepository userRepo;

    @Test
    void findById_checkValidSQL() {
        // arrange
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<User>>any())).thenReturn(List.of(new User()));

        // act
        userRepo.findById(10);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<User>>any());

        // assert
        assertEquals("SELECT * FROM user WHERE id = :id", sqlCaptor.getValue());
        assertEquals(10, paramCaptor.getValue().getValue("id"));
    }
    @Nested
    class findAll{
        @Test
        void whenNoPage_SQLShouldSelectAll() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<User>>any())).thenReturn(List.of(new User()));

            // act
            userRepo.findAll(null);

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<User>>any());

            // assert
            assertEquals("SELECT * FROM user ORDER BY id", sqlCaptor.getValue());
        }
        @Test
        void whenPage_SQLShouldSelectWithLimitOffset() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<User>>any())).thenReturn(List.of(new User()));

            // act
            userRepo.findAll(new PageOptions(5, 10));

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<User>>any());

            // assert
            assertEquals("SELECT * FROM user ORDER BY id LIMIT :limit OFFSET :offset", sqlCaptor.getValue());
            assertEquals(5+1, paramCaptor.getValue().getValue("limit"));
            assertEquals(5*10, paramCaptor.getValue().getValue("offset"));
        }
    }
    @Test
    void existsById_checkValidSQL() {
        // arrange
        when(jdbcTemplate.queryForList(anyString(), any(SqlParameterSource.class))).thenReturn(List.of(new HashMap<>()));

        // act
        userRepo.existsById(10);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).queryForList(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("SELECT id FROM user WHERE id = ? LIMIT 1", sqlCaptor.getValue());
        assertEquals(10, paramCaptor.getValue().getValue("id"));
    }
    @Test
    void update_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        userRepo.update(JaneDoe);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("""
                    UPDATE user SET
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
        userRepo.insert(JaneDoe);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("INSERT INTO user (email, gender, first_name, last_name) VALUES (:email, :gender, :first_name, :last_name)\n", sqlCaptor.getValue());
        assertEquals("Doe", paramCaptor.getValue().getValue("last_name"));
    }

    @Test
    void deleteById_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        userRepo.deleteById(10);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals("DELETE FROM user WHERE id = :id", sqlCaptor.getValue());
        assertEquals(10, paramCaptor.getValue().getValue("id"));
    }
    @Nested
    class searchByName{
        @Test
        void whenNoPage_SQLShouldSelectINSTR() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<User>>any())).thenReturn(List.of(new User()));

            // act
            userRepo.searchByName("abc",  null);

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<User>>any());

            // assert
            assertEquals("SELECT * FROM user WHERE INSTR(CONCAT(first_name, ' ', last_name), :hint) > 0 ORDER BY id", sqlCaptor.getValue());
            assertEquals("abc", paramCaptor.getValue().getValue("hint"));
        }
        @Test
        void whenPage_SQLShouldAddLimitAndOffset() {
            // arrange
            when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<User>>any())).thenReturn(List.of(new User()));

            // act
            userRepo.searchByName("abc",  new PageOptions(10, 1));

            // capture
            ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
            verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<User>>any());

            // assert
            assertEquals("SELECT * FROM user WHERE INSTR(CONCAT(first_name, ' ', last_name), :hint) > 0 ORDER BY id LIMIT :limit OFFSET :offset", sqlCaptor.getValue());
            assertEquals("abc", paramCaptor.getValue().getValue("hint"));
            assertEquals(11, paramCaptor.getValue().getValue("limit"));
            assertEquals(0, paramCaptor.getValue().getValue("offset"));
        }
    }
}