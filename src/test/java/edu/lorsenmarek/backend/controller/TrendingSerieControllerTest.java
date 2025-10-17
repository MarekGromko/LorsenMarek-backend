package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.config.MockSecurityConfig;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.service.TrendingSerieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static edu.lorsenmarek.backend.service.TrendingSerieService.*;

@WebMvcTest(controllers = {TrendingSerieController.class, CommonErrorHandlerController.class})
@Import(MockSecurityConfig.class)
class TrendingSerieControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    TrendingSerieService mockTrendingSerieService;
    @MockitoBean
    MockSecurityConfig.AuthMiddleware mockAuthMiddleware;
    @Test
    void getTrendingSerie_shouldRespond200WithListOfTrendingSerie() throws Exception {
        // arrange
        when(mockAuthMiddleware.getAuthToken()).thenReturn(Optional.empty());
        when(mockTrendingSerieService.getTrendingSeries()).thenReturn(List.of(
                new SerieTrendingScore(new Serie(1L, "SerieA", Instant.now()), 10.0),
                new SerieTrendingScore(new Serie(1L, "SerieA", Instant.now()), 9.0)
        ));

        // act
        var result = mockMvc.perform(get("/public/trending/serie"));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(10.0))
                .andExpect(jsonPath("$[1].score").value(9.0));
    }
}