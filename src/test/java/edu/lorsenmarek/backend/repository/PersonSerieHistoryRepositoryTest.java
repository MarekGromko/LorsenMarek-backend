package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.PersonSerieHistory;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonSerieHistoryRepositoryTest {
    final PersonSerieHistory PSHStub = PersonSerieHistory.builder()
            .personId(1)
            .serieId(2)
            .instanceWatch(5)
            .lastWatch(Instant.now())
            .build();
    @Mock
    NamedParameterJdbcTemplate jdbcTemplate;
    @InjectMocks
    PersonSerieHistoryRepository PSHRepo;
    @Test
    void findByPerson_checkValidSQL() {
        // arrange
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<PersonSerieHistory>>any()))
                .thenReturn(List.of(PSHStub));

        // act
        PSHRepo.findByPerson(1);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<PersonSerieHistory>>any());

        // assert
        assertEquals(sqlCaptor.getValue(), "SELECT * FROM person_serie_history WHERE person_id = :id");
        assertEquals(paramCaptor.getValue().getValue("id"), 1);
    }
    @Test
    void findBySerie_checkValidSQL() {
        // arrange
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<PersonSerieHistory>>any()))
                .thenReturn(List.of(PSHStub));

        // act
        PSHRepo.findBySerie(1);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<PersonSerieHistory>>any());

        // assert
        assertEquals(sqlCaptor.getValue(), "SELECT * FROM person_serie_history WHERE serie_id = :id");
        assertEquals(paramCaptor.getValue().getValue("id"), 1);
    }
    @Test
    void existsByIds_checkValidSQL() {
        // arrange
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), ArgumentMatchers.<RowMapper<PersonSerieHistory>>any()))
                .thenReturn(List.of(PSHStub));

        // act
        PSHRepo.existsByIds(1, 2);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).query(sqlCaptor.capture(), paramCaptor.capture(), ArgumentMatchers.<RowMapper<PersonSerieHistory>>any());

        // assert
        assertEquals(sqlCaptor.getValue(), "SELECT * FROM person_serie_history WHERE serie_id = :serie_id AND person_id = :person_id LIMIT 1");
        assertEquals(paramCaptor.getValue().getValue("person_id"), 1);
        assertEquals(paramCaptor.getValue().getValue("serie_id"), 2);
    }
    @Test
    void update_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        PSHRepo.update(PSHStub);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals(sqlCaptor.getValue(), """
                    UPDATE person_serie_history SET
                        last_watch = :last_watch
                        instance_watch = :instance_watch
                    WHERE
                        person_id = :person_id AND
                        serie_id = :serie_id""");
        assertEquals(paramCaptor.getValue().getValue("person_id"), 1);
        assertEquals(paramCaptor.getValue().getValue("serie_id"), 2);
        assertEquals(paramCaptor.getValue().getValue("last_watch"), PSHStub.getLastWatch());
    }
    @Test
    void insert_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        PSHRepo.insert(PSHStub);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals(sqlCaptor.getValue(), """
                    INSERT INTO person_serie_history (person_id, serie_id, last_watch, instance_watch) VALUES
                    (:person_id, :serie_id, :last_watch, :instance_watch)""");
        assertEquals(paramCaptor.getValue().getValue("person_id"), 1);
        assertEquals(paramCaptor.getValue().getValue("serie_id"), 2);
        assertEquals(paramCaptor.getValue().getValue("last_watch"), PSHStub.getLastWatch());
    }
    @Test
    void deleteByIds_checkValidSQL() {
        // arrange
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // act
        PSHRepo.deleteByIds(1, 2);

        // capture
        ArgumentCaptor<String> sqlCaptor               = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramCaptor.capture());

        // assert
        assertEquals(sqlCaptor.getValue(), """
                    DELETE FROM person_serie_history WHERE person_id = :person_id AND serie_id = :serie_id""");
        assertEquals(paramCaptor.getValue().getValue("person_id"), 1);
        assertEquals(paramCaptor.getValue().getValue("serie_id"), 2);
    }
}