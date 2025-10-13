package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.dto.RatingRequest;
import edu.lorsenmarek.backend.exception.RatingUnwatchedMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.security.JwtHttpFilter;
import edu.lorsenmarek.backend.security.token.DetailedAuthToken;
import edu.lorsenmarek.backend.service.EpisodeRatingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EpisodeRatingControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    EpisodeRatingService mockEpisodeRatingService;
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
            auth.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(auth);
            ((FilterChain) inv.getArgument(2)).doFilter(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(mockJwtHttpFilter).doFilter(
                any(ServletRequest.class),
                any(ServletResponse.class),
                any(FilterChain.class)
        );
    }
    @Nested
    class GetEpisodeRating {
        @Test
        void whenEpisodeDoesNotExist_shouldRespond404ResourceNotFound() throws Exception {
            // arrange
            doThrow(new ResourceNotFoundException("episode"))
                    .when(mockEpisodeRatingService)
                    .getMeanRating(anyLong());
            // act
            var result = mockMvc.perform(get("/rating/episode/1"));

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
        @Test
        void whenEpisodeExists_shouldRespond202WithMeanValue() throws Exception {
            // arrange
            when(mockEpisodeRatingService.getMeanRating(anyLong())).thenReturn(new MeanValue(1L, 27L));

            // act
            var result = mockMvc.perform(get("/rating/episode/1"));

            // assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("count").value(27));
        }
        @Test
        void checkValidArguments() throws Exception{
            // arrange
            when(mockEpisodeRatingService.getMeanRating(anyLong())).thenReturn(new MeanValue(1L, 27L));

            // act
            var result = mockMvc.perform(get("/rating/episode/8"));

            // capture
            var episodeIdCaptor = ArgumentCaptor.forClass(Long.class);
            verify(mockEpisodeRatingService).getMeanRating(episodeIdCaptor.capture());

            // assert
            assertEquals(episodeIdCaptor.getValue(), 8);
        }
    }
    @Nested
    class SaveEpisodeRating {
        ResultActions performWellFormedRequest() throws Exception{
            return mockMvc.perform(put("/rating/episode/8")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(new RatingRequest(5))));
        }
        @Test
        void whenEpisodeDoesNotExists_shouldRespond404ResourceNotFound() throws Exception {
            // arrange
            doThrow(new ResourceNotFoundException("episode"))
                    .when(mockEpisodeRatingService)
                    .tryRating(anyLong(), anyLong(), anyInt());

            // act
            var result = performWellFormedRequest();

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
        @Test
        void whenEpisodeDoesExistsButUserHasNotYetWatched_shouldRespondWith403RatingUnwatchedMedia() throws Exception {
            // arrange
            doThrow(new RatingUnwatchedMediaException())
                    .when(mockEpisodeRatingService)
                    .tryRating(anyLong(), anyLong(), anyInt());

            // act
            var result = performWellFormedRequest();

            // assert
            result.andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value("RatingUnwatchedMedia"));
        }
        @Test
        void whenEpisodeExistsAndEpisodeIsWatched_shouldRespondWith204() throws Exception {
            // arrange
            doNothing()
                    .when(mockEpisodeRatingService)
                    .tryRating(anyLong(), anyLong(), anyInt());
            // act
            var result = performWellFormedRequest();

            // assert
            result.andExpect(status().isNoContent());
        }
        @Test
        void checkValidArguments() throws Exception {
            // arrange
            doNothing()
                    .when(mockEpisodeRatingService)
                    .tryRating(anyLong(), anyLong(), anyInt());
            // act
            var result = performWellFormedRequest();

            // capture
            var userIdCaptor    = ArgumentCaptor.forClass(Long.class);
            var episodeIdCaptor   = ArgumentCaptor.forClass(Long.class);
            var ratingCaptor    = ArgumentCaptor.forClass(Integer.class);
            verify(mockEpisodeRatingService).tryRating(
                    userIdCaptor.capture(),
                    episodeIdCaptor.capture(),
                    ratingCaptor.capture()
            );
            // assert
            assertEquals(userIdCaptor.getValue(), 1L);
            assertEquals(episodeIdCaptor.getValue(), 8L);
            assertEquals(ratingCaptor.getValue(), 5);
        }
    }
    @Test
    void deleteEpisodeRating_checkValidArguments() throws Exception{
        // arrange
        doNothing()
                .when(mockEpisodeRatingService)
                .deleteRating(anyLong(), anyLong());

        // act
        var result = mockMvc.perform(delete("/rating/episode/8"));

        // capture
        var userIdCaptor    = ArgumentCaptor.forClass(Long.class);
        var episodeIdCaptor   = ArgumentCaptor.forClass(Long.class);
        verify(mockEpisodeRatingService).deleteRating(userIdCaptor.capture(), episodeIdCaptor.capture());

        // assert
        assertEquals(userIdCaptor.getValue(), 1L);
        assertEquals(episodeIdCaptor.getValue(), 8L);
    }
}