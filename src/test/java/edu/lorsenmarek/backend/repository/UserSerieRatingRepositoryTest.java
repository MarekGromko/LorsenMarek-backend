package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.UserSerieRating;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserSerieRatingRepositoryTest {
    @Mock
    JdbcTemplate mockJdbc;
    @InjectMocks
    UserSerieRatingRepository userSerieRatingRepo;
    UserSerieRating USRStub;
    @BeforeEach
    void populateStubs() {
        USRStub = UserSerieRating.builder()
                .userId(1L)
                .serieId(2L)
                .modifiedAt(Instant.parse("2007-12-03T10:15:30.00Z"))
                .createdAt(null)
                .rating(4)
                .build();
    }
    @Test
    void findByUserId_checkValidSQL() {
        // arrange
        when(mockJdbc.query(anyString(), ArgumentMatchers.<RowMapper<UserSerieRating>>any(), anyLong())).thenReturn(List.of(USRStub));

        // act
        userSerieRatingRepo.findByUserId(1L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> idCaptor       = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).query(sqlCaptor.capture(), ArgumentMatchers.<RowMapper<UserSerieRating>>any(), idCaptor.capture());

        // assert
        assertEquals("SELECT * FROM user_serie_rating WHERE user_id = ?", sqlCaptor.getValue());
        assertEquals(1L, idCaptor.getValue());
    }
    @Test
    void findBySerieId_checkValidSQL() {
        // arrange
        when(mockJdbc.query(anyString(), ArgumentMatchers.<RowMapper<UserSerieRating>>any(), anyLong())).thenReturn(List.of(USRStub));

        // act
        userSerieRatingRepo.findBySerieId(1L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> idCaptor       = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).query(sqlCaptor.capture(), ArgumentMatchers.<RowMapper<UserSerieRating>>any(), idCaptor.capture());

        // assert
        assertEquals("SELECT * FROM user_serie_rating WHERE serie_id = ?", sqlCaptor.getValue());
        assertEquals(1L, idCaptor.getValue());
    }
    @Test
    void findByUseridAndSerieId_checkValidSQL() {
        // arrange
        when(mockJdbc.query(
                anyString(),
                ArgumentMatchers.<RowMapper<UserSerieRating>>any(),
                anyLong(),
                anyLong()
        )).thenReturn(List.of(USRStub));

        // act
        userSerieRatingRepo.findByUserIdAndSerieId(1L, 2L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> serieIdCaptor  = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).query(
                sqlCaptor.capture(),
                ArgumentMatchers.<RowMapper<UserSerieRating>>any(),
                userIdCaptor.capture(),
                serieIdCaptor.capture()
        );

        // assert
        assertEquals("SELECT * FROM user_serie_rating WHERE user_id = ? AND serie_id = ?", sqlCaptor.getValue());
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, serieIdCaptor.getValue());
    }
    @Test
    void insert_checkValidSQL() {
        // arrange
        /// NONE

        // act
        userSerieRatingRepo.insert(USRStub);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> serieIdCaptor  = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> ratingCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                userIdCaptor.capture(),
                serieIdCaptor.capture(),
                any(),
                any(),
                ratingCaptor.capture()
        );

        // assert
        assertEquals("""
                INSERT INTO user_serie_rating
                (user_id, serie_id, created_at, modified_at, rating)
                VALUES (?,?,?,?,?)""",
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, serieIdCaptor.getValue());
        assertEquals(4, ratingCaptor.getValue());
    }
    @Test
    void update_checkValidSQL() {
        // arrange
        /// NONE

        // act
        userSerieRatingRepo.update(USRStub);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> serieIdCaptor  = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> ratingCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                any(),
                any(),
                ratingCaptor.capture(),
                userIdCaptor.capture(),
                serieIdCaptor.capture()
        );

        // assert
        assertEquals("""
                UPDATE user_serie_rating
                SET created_at = ?, modified_at = ?, rating = ?
                WHERE user_id = ? AND serie_id = ?""",
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, serieIdCaptor.getValue());
        assertEquals(4, ratingCaptor.getValue());
    }
    @Test
    void deleteByUserIdAndSerieId_checkValidSQL() {
        // arrange
        /// NONE

        // act
        userSerieRatingRepo.deleteByUserIdAndSerieId(1L, 2L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> serieIdCaptor  = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                userIdCaptor.capture(),
                serieIdCaptor.capture()
        );

        // assert
        assertEquals("""
                DELETE FROM user_serie_rating
                WHERE user_id = ? AND serie_id = ?
                """,
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, serieIdCaptor.getValue());
    }
}