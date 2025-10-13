package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.dto.RatingRequest;
import edu.lorsenmarek.backend.exception.RatingUnseenMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.security.JwtHttpFilter;
import edu.lorsenmarek.backend.security.token.DetailedAuthToken;
import edu.lorsenmarek.backend.service.SerieRatingService;
import edu.lorsenmarek.backend.util.JwtUtil;
import jakarta.servlet.*;
import org.junit.jupiter.api.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SerieRatingControllerTest {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    SerieRatingService mockSerieRatingService;
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
    class GetSerieRating {
        @Test
        void whenSerieDoesNotExist_shouldRespond404ResourceNotFound() throws Exception {
            // arrange
            doThrow(new ResourceNotFoundException("serie"))
                    .when(mockSerieRatingService)
                    .getMeanRating(anyLong());
            // act
            var result = mockMvc.perform(get("/rating/serie/1"));

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
        @Test
        void whenSerieExists_shouldRespond202WithMeanValue() throws Exception {
            // arrange
            when(mockSerieRatingService.getMeanRating(anyLong())).thenReturn(new MeanValue(1L, 27L));

            // act
            var result = mockMvc.perform(get("/rating/serie/1"));

            // assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("count").value(27));
        }
        @Test
        void checkValidArguments() throws Exception{
            // arrange
            when(mockSerieRatingService.getMeanRating(anyLong())).thenReturn(new MeanValue(1L, 27L));

            // act
            var result = mockMvc.perform(get("/rating/serie/8"));

            // capture
            var serieIdCaptor = ArgumentCaptor.forClass(Long.class);
            verify(mockSerieRatingService).getMeanRating(serieIdCaptor.capture());

            // assert
            assertEquals(serieIdCaptor.getValue(), 8);
        }
    }
    @Nested
    class SaveSerieRating {
        ResultActions performWellFormedRequest() throws Exception{
            return mockMvc.perform(put("/rating/serie/8")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(new RatingRequest(5))));
        }
        @Test
        void whenSerieDoesNotExists_shouldRespond404ResourceNotFound() throws Exception {
            // arrange
            doThrow(new ResourceNotFoundException("serie"))
                    .when(mockSerieRatingService)
                    .tryRating(anyLong(), anyLong(), anyInt());

            // act
            var result = performWellFormedRequest();

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
        @Test
        void whenSerieDoesExistsButUserHasNotSeenIt_shouldRespondWith403RatingUnseenMedia() throws Exception {
            // arrange
            doThrow(new RatingUnseenMediaException())
                    .when(mockSerieRatingService)
                    .tryRating(anyLong(), anyLong(), anyInt());

            // act
            var result = performWellFormedRequest();

            // assert
            result.andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value("RatingUnseenMedia"));
        }
        @Test
        void whenSerieExistsAndSerieIsSeen_shouldRespondWith204() throws Exception {
            // arrange
            doNothing()
                    .when(mockSerieRatingService)
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
                    .when(mockSerieRatingService)
                    .tryRating(anyLong(), anyLong(), anyInt());
            // act
            var result = performWellFormedRequest();

            // capture
            var userIdCaptor    = ArgumentCaptor.forClass(Long.class);
            var serieIdCaptor   = ArgumentCaptor.forClass(Long.class);
            var ratingCaptor    = ArgumentCaptor.forClass(Integer.class);
            verify(mockSerieRatingService).tryRating(
                    userIdCaptor.capture(),
                    serieIdCaptor.capture(),
                    ratingCaptor.capture()
            );
            // assert
            assertEquals(userIdCaptor.getValue(), 1L);
            assertEquals(serieIdCaptor.getValue(), 8L);
            assertEquals(ratingCaptor.getValue(), 5);
        }
    }
    @Test
    void deleteSerieRating_checkValidArguments() throws Exception{
        // arrange
        doNothing()
                .when(mockSerieRatingService)
                .deleteRating(anyLong(), anyLong());

        // act
        var result = mockMvc.perform(delete("/rating/serie/8"));

        // capture
        var userIdCaptor    = ArgumentCaptor.forClass(Long.class);
        var serieIdCaptor   = ArgumentCaptor.forClass(Long.class);
        verify(mockSerieRatingService).deleteRating(userIdCaptor.capture(), serieIdCaptor.capture());

        // assert
        assertEquals(userIdCaptor.getValue(), 1L);
        assertEquals(serieIdCaptor.getValue(), 8L);
    }
}