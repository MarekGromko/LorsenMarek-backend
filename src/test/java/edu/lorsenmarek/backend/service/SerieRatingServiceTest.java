package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.common.MeanValue;
import edu.lorsenmarek.backend.exception.RatingUnwatchedMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.UserSerieRating;
import edu.lorsenmarek.backend.repository.SerieRepository;
import edu.lorsenmarek.backend.repository.UserSerieRatingRepository;
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

import static edu.lorsenmarek.backend.repository.UserSerieRatingRepository.Ids;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SerieRatingServiceTest {
    @Mock
    JdbcTemplate mockJdbc;
    @Mock
    SerieRepository mockSerieRepo;
    @Mock
    UserSerieRatingRepository mockUserSerieRatingRepo;
    @Mock
    UserMediaHistoryService mockUserMediaHistoryService;
    @InjectMocks
    SerieRatingService serieRatingService;
    UserSerieRating USRStub;
    @BeforeEach
    void populateStub() {
        USRStub = UserSerieRating.builder()
                .userId(1L)
                .serieId(1L)
                .rating(4)
                .createdAt(Instant.parse("2007-12-03T10:15:30.00Z"))
                .modifiedAt(null)
                .build();
    }
    @Nested
    class TryRating {
        @Test
        void whenSerieDoesNotExist_shouldThrow() {
            // arrange
            when(mockSerieRepo.existsById(anyLong())).thenReturn(false);

            // act & assert
            assertThrows(ResourceNotFoundException.class, ()->{
               serieRatingService.tryRating(1L, 1L, 4);
            });
        }
        @Test
        void whenSerieIsNotYetWatched_shouldThrow() {
            // arrange
            when(mockSerieRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserMediaHistoryService.hasWatchedSerie(anyLong(), anyLong())).thenReturn(false);

            // act & assert
            assertThrows(RatingUnwatchedMediaException.class, ()->{
                serieRatingService.tryRating(1L, 1L, 4);
            });
        }
        @Test
        void whenRatingExists_shouldUpdateModifiedAt() {
            // arrange
            when(mockSerieRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserMediaHistoryService.hasWatchedSerie(anyLong(), anyLong())).thenReturn(true);
            when(mockUserSerieRatingRepo.findOneByIds(any())).thenReturn(Optional.of(USRStub));

            // act
            serieRatingService.tryRating(1L, 2L, 4);

            // capture
            ArgumentCaptor<UserSerieRating> USRCaptor = ArgumentCaptor.forClass(UserSerieRating.class);
            verify(mockUserSerieRatingRepo).update(USRCaptor.capture());

            // assert
            assertEquals(
                    InstantCodecUtil.encode(USRCaptor.getValue().getModifiedAt()),
                    InstantCodecUtil.encode(Instant.now())
            );
        }
        @Test
        void whenRatingDoesNotExist_shouldInsertRating() {
            // arrange
            when(mockSerieRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserMediaHistoryService.hasWatchedSerie(anyLong(), anyLong())).thenReturn(true);
            when(mockUserSerieRatingRepo.findOneByIds(any(Ids.class))).thenReturn(Optional.empty());

            // act
            serieRatingService.tryRating(1L, 2L, 4);

            // capture
            ArgumentCaptor<UserSerieRating> USRCaptor = ArgumentCaptor.forClass(UserSerieRating.class);
            verify(mockUserSerieRatingRepo).insert(USRCaptor.capture());

            // assert
            assertEquals(
                    InstantCodecUtil.encode(USRCaptor.getValue().getCreatedAt()),
                    InstantCodecUtil.encode(Instant.now())
            );
        }
    }
    @Test
    void deleteRating_shouldCallUserSerieRatingRepoDelete() {
        // arrange
        when(mockUserSerieRatingRepo.deleteByIds(any(Ids.class))).thenReturn(1);

        // act
        serieRatingService.deleteRating(1L, 2L);

        // capture
        var idsCaptor   = ArgumentCaptor.forClass(Ids.class);
        verify(mockUserSerieRatingRepo).deleteByIds(idsCaptor.capture());

        // assert
        assertEquals(idsCaptor.getValue().userId(), 1L);
        assertEquals(idsCaptor.getValue().serieId(), 2L);
    }
    @Nested
    class GetMeanRating {
        @Test
        void whenSerieDoesNotExist_shouldThrow() {
            // arrange
            when(mockSerieRepo.existsById(anyLong())).thenReturn(false);

            // act & assert
            assertThrows(ResourceNotFoundException.class, ()->{
               serieRatingService.getMeanRating(1L);
            });

        }
        @Test
        void whenSerieExistsAndNoRating_shouldGetMeanValueWithZeroCount() {
            // arrange
            when(mockSerieRepo.existsById(anyLong())).thenReturn(true);
            when(mockJdbc.query(
                    anyString(),
                    ArgumentMatchers.<RowMapper<MeanValue>>any(),
                    anyLong())
            ).thenReturn(Collections.emptyList());

            // act
            var meanValue = serieRatingService.getMeanRating(1L);

            // assert
            assertEquals(meanValue.count(), 0);
        }
        @Test
        void whenSerieAndRatingExists_shouldGetMeanValue() {
            // arrange
            when(mockSerieRepo.existsById(anyLong())).thenReturn(true);
            when(mockUserSerieRatingRepo.findByIds(any(Ids.class))).thenReturn(List.of(USRStub));
            when(mockJdbc.query(
                    anyString(),
                    ArgumentMatchers.<RowMapper<MeanValue>>any(),
                    anyLong())
            ).thenReturn(List.of(new MeanValue(2, 1)));

            // act
            var meanValue = serieRatingService.getMeanRating(1L);

            // assert
            assertEquals(meanValue.getValue(), 2);
        }
    }

}