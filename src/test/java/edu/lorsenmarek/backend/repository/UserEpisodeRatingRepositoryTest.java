package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.UserEpisodeRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserEpisodeRatingRepositoryTest {
    @Mock
    JdbcTemplate mockJdbc;
    @InjectMocks
    UserEpisodeRatingRepository userEpisodeRatingRepo;
    UserEpisodeRating UERStub;
    @BeforeEach
    void populateStubs() {
        UERStub = UserEpisodeRating.builder()
                .userId(1L)
                .episodeId(2L)
                .modifiedAt(Instant.parse("2007-12-03T10:15:30.00Z"))
                .createdAt(null)
                .rating(4)
                .build();
    }
    @Test
    void findByUserId_checkValidSQL() {
        // arrange
        when(mockJdbc.query(anyString(), ArgumentMatchers.<RowMapper<UserEpisodeRating>>any(), anyLong())).thenReturn(List.of(UERStub));

        // act
        userEpisodeRatingRepo.findByUserId(1L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> idCaptor       = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).query(sqlCaptor.capture(), ArgumentMatchers.<RowMapper<UserEpisodeRating>>any(), idCaptor.capture());

        // assert
        assertEquals("SELECT * FROM user_episode_rating WHERE user_id = ?", sqlCaptor.getValue());
        assertEquals(1L, idCaptor.getValue());
    }
    @Test
    void findByEpisodeId_checkValidSQL() {
        // arrange
        when(mockJdbc.query(anyString(), ArgumentMatchers.<RowMapper<UserEpisodeRating>>any(), anyLong())).thenReturn(List.of(UERStub));

        // act
        userEpisodeRatingRepo.findByEpisodeId(1L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> idCaptor       = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).query(sqlCaptor.capture(), ArgumentMatchers.<RowMapper<UserEpisodeRating>>any(), idCaptor.capture());

        // assert
        assertEquals("SELECT * FROM user_episode_rating WHERE episode_id = ?", sqlCaptor.getValue());
        assertEquals(1L, idCaptor.getValue());
    }
    @Test
    void findByUseridAndEpisodeId_checkValidSQL() {
        // arrange
        when(mockJdbc.query(
                anyString(),
                ArgumentMatchers.<RowMapper<UserEpisodeRating>>any(),
                anyLong(),
                anyLong()
        )).thenReturn(List.of(UERStub));

        // act
        userEpisodeRatingRepo.findByUserIdAndEpisodeId(1L, 2L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> episodeIdCaptor  = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).query(
                sqlCaptor.capture(),
                ArgumentMatchers.<RowMapper<UserEpisodeRating>>any(),
                userIdCaptor.capture(),
                episodeIdCaptor.capture()
        );

        // assert
        assertEquals("SELECT * FROM user_episode_rating WHERE user_id = ? AND episode_id = ?", sqlCaptor.getValue());
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, episodeIdCaptor.getValue());
    }
    @Test
    void insert_checkValidSQL() {
        // arrange
        /// NONE

        // act
        userEpisodeRatingRepo.insert(UERStub);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> episodeIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> ratingCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                userIdCaptor.capture(),
                episodeIdCaptor.capture(),
                any(),
                any(),
                ratingCaptor.capture()
        );

        // assert
        assertEquals("""
                       INSERT INTO user_episode_rating
                       (user_id, episode_id, created_at, modified_at, rating)
                       VALUES (?,?,?,?,?)""",
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, episodeIdCaptor.getValue());
        assertEquals(4, ratingCaptor.getValue());
    }
    @Test
    void update_checkValidSQL() {
        // arrange
        /// NONE

        // act
        userEpisodeRatingRepo.update(UERStub);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> episodeIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> ratingCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                any(),
                any(),
                ratingCaptor.capture(),
                userIdCaptor.capture(),
                episodeIdCaptor.capture()
        );

        // assert
        assertEquals("""
                UPDATE user_episode_rating
                SET created_at = ?, modified_at = ?, rating = ?
                WHERE user_id = ? AND episode_id = ?""",
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, episodeIdCaptor.getValue());
        assertEquals(4, ratingCaptor.getValue());
    }
    @Test
    void deleteByUserIdAndEpisodeId_checkValidSQL() {
        // arrange
        /// NONE

        // act
        userEpisodeRatingRepo.deleteByUserIdAndEpisodeId(1L, 2L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> episodeIdCaptor  = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).update(
                sqlCaptor.capture(),
                userIdCaptor.capture(),
                episodeIdCaptor.capture()
        );

        // assert
        assertEquals("""
                DELETE FROM user_episode_rating
                WHERE user_id = ? AND episode_id = ?
                """,
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, episodeIdCaptor.getValue());
    }
}