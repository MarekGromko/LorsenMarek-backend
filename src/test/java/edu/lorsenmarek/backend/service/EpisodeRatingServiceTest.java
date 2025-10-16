package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.exception.RatingUnwatchedMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.UserEpisodeRating;
import edu.lorsenmarek.backend.repository.EpisodeRepository;
import edu.lorsenmarek.backend.repository.UserEpisodeRatingRepository;
import edu.lorsenmarek.backend.util.InstantCodecUtil;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static edu.lorsenmarek.backend.repository.UserEpisodeRatingRepository.Ids;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
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
            when(mockUserEpisodeRatingRepo.findOneByIds(any(Ids.class))).thenReturn(Optional.of(UERStub));

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
            when(mockUserEpisodeRatingRepo.findOneByIds(any(Ids.class))).thenReturn(Optional.empty());

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
        when(mockUserEpisodeRatingRepo.deleteByIds(any(Ids.class))).thenReturn(1);

        // act
        episodeRatingService.deleteRating(1L, 2L);

        // capture
        var idsCaptor = ArgumentCaptor.forClass(Ids.class);
        verify(mockUserEpisodeRatingRepo).deleteByIds(idsCaptor.capture());

        // assert
        assertEquals(idsCaptor.getValue().userId(), 1L);
        assertEquals(idsCaptor.getValue().episodeId(), 2L);
    }
    @Nested
    class GetMeanRating {
        @Test
        void whenEpisodeDoesNotExist_shouldThrow() {
            // arrange
            when(mockEpisodeRepo.existsById(anyLong())).thenReturn(false);

            // act & assert
            assertThrows(ResourceNotFoundException.class, ()->{
               episodeRatingService.getMeanRating(1L);
            });

        }
        @Test
        void whenEpisodeExistsAndNoRating_shouldGetMeanValueWithZeroCount() {
            // arrange
            when(mockEpisodeRepo.existsById(anyLong())).thenReturn(true);
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
            when(mockEpisodeRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserEpisodeRatingRepo.findByIds(any(Ids.class))).thenReturn(List.of(UERStub));
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