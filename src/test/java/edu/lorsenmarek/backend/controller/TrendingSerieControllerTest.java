package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.security.JwtHttpFilter;
import edu.lorsenmarek.backend.security.token.DetailedAuthToken;
import edu.lorsenmarek.backend.service.SerieRatingService;
import edu.lorsenmarek.backend.service.TrendingSerieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static edu.lorsenmarek.backend.service.TrendingSerieService.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrendingSerieControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    TrendingSerieService mockTrendingSerieService;
    @MockitoBean
    JwtHttpFilter mockJwtHttpFilter;
    User userStub;
    @BeforeEach
    void mockJwtFilter() throws ServletException, IOException {
        userStub = User.builder()
                .id(1L)
                .build();

        doAnswer((inv)->{
            var auth = new DetailedAuthToken(userStub, Collections.emptyList());
            //Should work despite the user not being label as authenticated
            //auth.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(auth);
            ((FilterChain) inv.getArgument(2)).doFilter(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(mockJwtHttpFilter).doFilter(
                any(ServletRequest.class),
                any(ServletResponse.class),
                any(FilterChain.class)
        );
    }
    @Test
    void getTrendingSerie_shouldRespond200WithListOfTrendingSerie() throws Exception {
        // arrange
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