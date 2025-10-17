package edu.lorsenmarek.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.dto.EmailPasswordLoginRequest;
import edu.lorsenmarek.backend.dto.SigninRequest;
import edu.lorsenmarek.backend.model.*;
import edu.lorsenmarek.backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    /** Bogus users */
    static class Users {
        final static User bogus = User.builder().id(1L)
                .email("bogus@bogus.com").pwdDigest("testPassword").build();
        final static User present = User.builder()
                .email("normal@testuser.com").pwdDigest("testPassword").build();
        final static User lockout = User.builder()
                .email("lockout@testuser.com").pwdDigest("testPassword").build();
        final static User absent = User.builder().email("absent@testuser.com").build();
    }
    /** Bogus episodes */
    static class Episodes {
        final static Episode bogus = Episode.builder()
                .id(1L).serieId(1L).seasonNb(1).build();
        final static Episode notWatched = Episode.builder()
                .id(2L).serieId(2L).seasonNb(1).build();
    }
    /** Bogus series */
    static class Series {
        final static Serie bogus = Serie.builder()
                .id(1L).build();
        final static Serie notWatched = Serie.builder()
                .id(2L).build();
    }
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    @Nested
    class AuthRoute {
        @Test
        void signin_shouldReturn204_whenEmailIsNotInUse() throws Exception {
            // arrange
            var req = new SigninRequest(
                    "test@user.tmp",
                    "testPassword",
                    "T",
                    "test_tmp",
                    "test_tmp"
            );

            // act
            var result = mockMvc.perform(post("/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(req))
            );

            // assert
            result.andExpect(status().isNoContent());
        }
        @Test
        void signin_shouldReturn409_whenEmailIsInUse() throws Exception {
            // arrange
            var req = new SigninRequest(
                    Users.present.getEmail(),
                    "testPassword",
                    "Sm.",
                    "So",
                    "Me"
            );

            // act
            var result = mockMvc.perform(post("/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(req))
            );

            // assert
            result.andExpect(status().isConflict())
                    .andExpect(jsonPath("code").value("EmailAlreadyInUse"));
        }
        @Test
        void login_shouldReturn200AndJwt_whenEverythingIsCorrect() throws Exception {
            // arrange
            var req = new EmailPasswordLoginRequest(
                    Users.present.getEmail(),
                    Users.present.getPwdDigest()
            );

            // act
            var result = mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(req))
            );

            // assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("jwt").exists());
        }
        @Test
        void login_shouldReturn403_whenUserIsLockout() throws Exception {
            // arrange
            var req = new EmailPasswordLoginRequest(
                    Users.lockout.getEmail(),
                    Users.lockout.getPwdDigest()
            );

            // act
            var result = mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(req))
            );

            // assert
            result.andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value("UserLocked"));
        }
        @Test
        void login_shouldReturn403_whenPasswordDoesNotMatch() throws Exception {
            // arrange
            var req = new EmailPasswordLoginRequest(
                    Users.present.getEmail(),
                    "bogusPassword"
            );

            // act
            var result = mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(req))
            );

            // assert
            result.andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value("BadPassword"));
        }
        @Test
        void login_shouldReturn404_whenEmailHasNotHit() throws Exception {
            // arrange
            var req = new EmailPasswordLoginRequest(
                    Users.absent.getEmail(),
                    "bogusPassword"
            );

            // act
            var result = mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(req))
            );

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("UserNotFound"));
        }
    }
    @Nested
    class EpisodeRatingRoute {
        HttpHeaders jwt;
        @BeforeEach
        void generateJwt() {
            var token = jwtUtil.generateToken(Users.bogus.getEmail());
            jwt = new HttpHeaders();
            jwt.set("Authorization", "Bearer " + token);
        }
        @Test
        void getEpisodeRating_shouldReturn200_whenEpisodeExists() throws Exception {
            // act
            var result = mockMvc.perform(get("/rating/episode/%d".formatted(Episodes.bogus.getId()))
                    .headers(jwt)
            );

            // assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("sum").exists())
                    .andExpect(jsonPath("count").exists());
        }
        @Test
        void getEpisodeRating_shouldReturn404_whenEpisodeNotFound() throws Exception {
            // act
            var result = mockMvc.perform(get("/rating/episode/999").headers(jwt));

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
        @Test
        void saveEpisodeRating_shouldReturn204_whenRatingWork() throws Exception {
            // arrange
            var content = "{\"rating\": 4}";

            // act
            var result = mockMvc.perform(put("/rating/episode/%d".formatted(Episodes.bogus.getId()))
                    .headers(jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // assert
            result.andExpect(status().isNoContent());
        }
        @Test
        void saveEpisodeRating_shouldReturn403_whenRatingUnwatchEpisode() throws Exception {
            // arrange
            var content = "{\"rating\": 4}";

            // act
            var result = mockMvc.perform(put("/rating/episode/%d".formatted(Episodes.notWatched.getId()))
                    .headers(jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // assert
            result.andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value("RatingUnwatchedMedia"));
        }
        @Test
        void saveEpisodeRating_shouldReturn404_whenEpisodeDoesNotExist() throws Exception {
            // arrange
            var content = "{\"rating\": 4}";

            // act
            var result = mockMvc.perform(put("/rating/episode/999")
                    .headers(jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
    }
    @Nested
    class SerieRatingRoute {
        HttpHeaders jwt;
        @BeforeEach
        void generateJwt() {
            var token = jwtUtil.generateToken(Users.bogus.getEmail());
            jwt = new HttpHeaders();
            jwt.set("Authorization", "Bearer " + token);
        }
        @Test
        void getSerieRating_shouldReturn200_whenSerieExists() throws Exception {
            // act
            var result = mockMvc.perform(get("/rating/serie/%d".formatted(Series.bogus.getId()))
                    .headers(jwt)
            );

            // assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("sum").exists())
                    .andExpect(jsonPath("count").exists());
        }
        @Test
        void getSerieRating_shouldReturn404_whenSerieNotFound() throws Exception {
            // act
            var result = mockMvc.perform(get("/rating/episode/999").headers(jwt));

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
        @Test
        void saveSerieRating_shouldReturn204_whenRatingWork() throws Exception {
            // arrange
            var content = "{\"rating\": 4}";

            // act
            var result = mockMvc.perform(put("/rating/serie/%d".formatted(Series.bogus.getId()))
                    .headers(jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // assert
            result.andExpect(status().isNoContent());
        }
        @Test
        void saveSerieRating_shouldReturn403_whenRatingUnwatchEpisode() throws Exception {
            // arrange
            var content = "{\"rating\": 4}";

            // act
            var result = mockMvc.perform(put("/rating/serie/%d".formatted(Series.notWatched.getId()))
                    .headers(jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // assert
            result.andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value("RatingUnwatchedMedia"));
        }
        @Test
        void saveEpisodeRating_shouldReturn404_whenEpisodeDoesNotExist() throws Exception {
            // arrange
            var content = "{\"rating\": 4}";

            // act
            var result = mockMvc.perform(put("/rating/serie/999")
                    .headers(jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value("ResourceNotFound"));
        }
    }
    @Nested
    class TrendingSerieRoute {
        @Test
        void getTrendingSerie_shouldReturn200WithListOfSerieAndScore() throws Exception{
            // act
            var result = mockMvc.perform(get("/public/trending/serie"));

            // assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].score").isNumber())
                    .andExpect(jsonPath("$[0].serie.id").value(Series.bogus.getId()))
                    .andExpect(jsonPath("$[1].score").isNumber());
        }
    }
}
