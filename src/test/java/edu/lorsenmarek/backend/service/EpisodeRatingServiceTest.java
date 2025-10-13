package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.exception.RatingUnwatchedMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.UserEpisodeRating;
import edu.lorsenmarek.backend.repository.EpisodeRepository;
import edu.lorsenmarek.backend.repository.UserEpisodeRatingRepository;
import edu.lorsenmarek.backend.util.InstantCodecUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EpisodeRatingServiceTest {
    @Mock
    JdbcTemplate mockJdbc;
    @Mock
    EpisodeRepository mockEpisodeRepo;
    @Mock
    UserEpisodeRatingRepository mockUserEpisodeRatingRepo;
    @Mock
    UserMediaHistoryService mockUserMediaHistoryService;
    @InjectMocks
    EpisodeRatingService episodeRatingService;
    UserEpisodeRating UERStub;
    @BeforeEach
    void populateStub() {
        UERStub = UserEpisodeRating.builder()
                .userId(1L)
                .episodeId(1L)
                .rating(4)
                .createdAt(Instant.parse("2007-12-03T10:15:30.00Z"))
                .modifiedAt(null)
                .build();
    }
    @Nested
    class TryRating {
        @Test
        void whenEpisodeDoesNotExist_shouldThrow() {
            // arrange
            when(mockEpisodeRepo.existsById(anyLong())).thenReturn(false);

            // act & assert
            assertThrows(ResourceNotFoundException.class, ()->{
               episodeRatingService.tryRating(1L, 1L, 4);
            });
        }
        @Test
        void whenEpisodeIsNotYetWatched_shouldThrow() {
            // arrange
            when(mockEpisodeRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserMediaHistoryService.hasWatchedEpisode(anyLong(), anyLong())).thenReturn(false);

            // act & assert
            assertThrows(RatingUnwatchedMediaException.class, ()->{
                episodeRatingService.tryRating(1L, 1L, 4);
            });
        }
        @Test
        void whenRatingExists_shouldUpdateModifiedAt() {
            // arrange
            when(mockEpisodeRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserMediaHistoryService.hasWatchedEpisode(anyLong(), anyLong())).thenReturn(true);
            when(mockUserEpisodeRatingRepo.findByUserIdAndEpisodeId(anyLong(), anyLong())).thenReturn(Optional.of(UERStub));

            // act
            episodeRatingService.tryRating(1L, 2L, 4);

            // capture
            ArgumentCaptor<UserEpisodeRating> UERCaptor = ArgumentCaptor.forClass(UserEpisodeRating.class);
            verify(mockUserEpisodeRatingRepo).update(UERCaptor.capture());

            // assert
            assertEquals(
                    InstantCodecUtil.encode(UERCaptor.getValue().getModifiedAt()),
                    InstantCodecUtil.encode(Instant.now())
            );
        }
        @Test
        void whenRatingDoesNotExist_shouldInsertRating() {
            // arrange
            when(mockEpisodeRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserMediaHistoryService.hasWatchedEpisode(anyLong(), anyLong())).thenReturn(true);
            when(mockUserEpisodeRatingRepo.findByUserIdAndEpisodeId(anyLong(), anyLong())).thenReturn(Optional.empty());

            // act
            episodeRatingService.tryRating(1L, 2L, 4);

            // capture
            ArgumentCaptor<UserEpisodeRating> UERCaptor = ArgumentCaptor.forClass(UserEpisodeRating.class);
            verify(mockUserEpisodeRatingRepo).insert(UERCaptor.capture());

            // assert
            assertEquals(
                    InstantCodecUtil.encode(UERCaptor.getValue().getCreatedAt()),
                    InstantCodecUtil.encode(Instant.now())
            );
        }
    }
    @Test
    void deleteRating_shouldCallUserEpisodeRatingRepoDelete() {
        // arrange
        when(mockUserEpisodeRatingRepo.deleteByUserIdAndEpisodeId(anyLong(), anyLong())).thenReturn(1);

        // act
        episodeRatingService.deleteRating(1L, 2L);

        // capture
        ArgumentCaptor<Long> userIdCaptor   = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> episodeIdCaptor  = ArgumentCaptor.forClass(Long.class);
        verify(mockUserEpisodeRatingRepo).deleteByUserIdAndEpisodeId(userIdCaptor.capture(), episodeIdCaptor.capture());

        // assert
        assertEquals(userIdCaptor.getValue(), 1L);
        assertEquals(episodeIdCaptor.getValue(), 2L);
    }
    @Nested
    class GetMeanRating {
        @Test
        void whenEpisodeDoesNotExist_shouldThrow() {
            // arrange
            when(mockUserEpisodeRatingRepo.findByEpisodeId(anyLong())).thenReturn(Collections.emptyList());

            // act & assert
            assertThrows(ResourceNotFoundException.class, ()->{
               episodeRatingService.getMeanRating(1L);
            });

        }
        @Test
        void whenEpisodeExistsAndNoRating_shouldGetMeanValueWithZeroCount() {
            // arrange
            when(mockUserEpisodeRatingRepo.findByEpisodeId(anyLong())).thenReturn(List.of(UERStub));
            when(mockJdbc.query(
                    anyString(),
                    ArgumentMatchers.<RowMapper<MeanValue>>any(),
                    anyLong())
            ).thenReturn(Collections.emptyList());

            // act
            var meanValue = episodeRatingService.getMeanRating(1L);

            // assert
            assertEquals(meanValue.count(), 0);
        }
        @Test
        void whenSerieAndRatingExists_shouldGetMeanValue() {
            // arrange
            when(mockUserEpisodeRatingRepo.findByEpisodeId(anyLong())).thenReturn(List.of(UERStub));
            when(mockJdbc.query(
                    anyString(),
                    ArgumentMatchers.<RowMapper<MeanValue>>any(),
                    anyLong())
            ).thenReturn(List.of(new MeanValue(2, 1)));

            // act
            var meanValue = episodeRatingService.getMeanRating(1L);

            // assert
            assertEquals(meanValue.getValue(), 2);
        }
    }

}