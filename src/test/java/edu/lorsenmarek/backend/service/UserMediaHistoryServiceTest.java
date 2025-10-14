package edu.lorsenmarek.backend.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserMediaHistoryServiceTest {
    @Mock
    JdbcTemplate mockJdbc;
    @InjectMocks
    UserMediaHistoryService UMHService;
    @Test
    void hasWatchedSerie_checkValidSQL() {
        // arrange
        when(mockJdbc.queryForObject(
                anyString(),
                ArgumentMatchers.<Class<Boolean>>any(),
                anyLong(),
                anyLong()
        )).thenReturn(true);

        // act
        UMHService.hasWatchedSerie(1L, 2L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> serieIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).queryForObject(
                sqlCaptor.capture(),
                ArgumentMatchers.<Class<Boolean>>any(),
                userIdCaptor.capture(),
                serieIdCaptor.capture()
        );

        // assert
        assertEquals("""
                SELECT EXISTS (
                    SELECT 1
                    FROM user_serie_history
                    WHERE
                        user_id = ? AND
                        serie_id = ?
                    LIMIT 1
                ) AS "exists"
                """,
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, serieIdCaptor.getValue());
    }
    @Test
    void hasWatchedEpisode_checkValidSQL() {
        // arrange
        when(mockJdbc.queryForObject(
                anyString(),
                ArgumentMatchers.<Class<Boolean>>any(),
                anyLong(),
                anyLong()
        )).thenReturn(true);

        // act
        UMHService.hasWatchedEpisode(1L, 2L);

        // capture
        ArgumentCaptor<String> sqlCaptor    = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> episodeIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(mockJdbc).queryForObject(
                sqlCaptor.capture(),
                ArgumentMatchers.<Class<Boolean>>any(),
                userIdCaptor.capture(),
                episodeIdCaptor.capture()
        );

        // assert
        assertEquals("""
                SELECT EXISTS (
                    SELECT 1
                    FROM user_episode_history
                    WHERE
                        user_id = ? AND
                        serie_id = ?
                    LIMIT 1
                ) AS "exists"
                """,
                sqlCaptor.getValue()
        );
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(2L, episodeIdCaptor.getValue());
    }
}