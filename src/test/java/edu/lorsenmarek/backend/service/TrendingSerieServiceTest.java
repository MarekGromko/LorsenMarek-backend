package edu.lorsenmarek.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TrendingSerieServiceTest {
    JdbcTemplate mockJdbc;
    TrendingSerieService trendingSerieService;
    @BeforeEach
    void createTrendingSerieService() {
        mockJdbc = mock(JdbcTemplate.class);
        trendingSerieService = new TrendingSerieService(mockJdbc, 1D, 1D, 10, "PT10M");
    }
    @Test
    void getTrendingSeries_checkValidSql() {
        // arrange
        when(mockJdbc.query(
                anyString(),
                ArgumentMatchers.<RowMapper<TrendingSerieService.SerieTrendingScore>>any(),
                anyDouble(),
                anyDouble(),
                anyInt()
        )).thenReturn(List.of());

        // act
        trendingSerieService.getTrendingSeries();

        // capture
        var sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockJdbc).query(
                sqlCaptor.capture(),
                ArgumentMatchers.<RowMapper<TrendingSerieService.SerieTrendingScore>>any(),
                anyDouble(),
                anyDouble(),
                anyInt()
        );

        // assert
        assertTrue(sqlCaptor.getValue().contains("trending_score"));
    }

}