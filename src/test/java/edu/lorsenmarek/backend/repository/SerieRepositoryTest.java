package edu.lorsenmarek.backend.repository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.lorsenmarek.backend.model.Serie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.*;

public class SerieRepositoryTest {

        @Mock
        private JdbcTemplate jdbcTemplate;

        @InjectMocks
        private edu.lorsenmarek.backend.repository.SerieRepository serieRepository;

        private Serie testSerie;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            testSerie = new Serie();
            testSerie.setId(1);
            testSerie.setTitle("Dark");
            testSerie.setGenre("Thriller");
            testSerie.setNb_episode(26);
            testSerie.setNote(8);
        }

        @Test
        void testFindAll() {
            List<Serie> expectedList = List.of(testSerie);

            when(jdbcTemplate.query(eq("SELECT * FROM serie"), any(RowMapper.class)))
                    .thenReturn(expectedList);

            List<Serie> result = serieRepository.findAll();

            assertEquals(1, result.size());
            assertEquals("Dark", result.get(0).getTitle());
        }

        @Test
        void testFindById_Found() {
            List<Serie> mockResult = List.of(testSerie);

            when(jdbcTemplate.query(eq("SELECT * FROM serie WHERE id = ?"), any(RowMapper.class), eq(1)))
                    .thenReturn(mockResult);

            Optional<Serie> result = serieRepository.findById(1);

            assertTrue(result.isPresent());
            assertEquals("Dark", result.get().getTitle());
        }

        @Test
        void testFindById_NotFound() {
            when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(999)))
                    .thenReturn(Collections.emptyList());

            Optional<Serie> result = serieRepository.findById(999);

            assertFalse(result.isPresent());
        }

        @Test
        void testSearchByTitle() {
            String sql = "SELECT * FROM serie WHERE LOWER(title) LIKE ?";
            String pattern = "%dark%";

            when(jdbcTemplate.query(eq(sql), any(RowMapper.class), eq(pattern)))
                    .thenReturn(List.of(testSerie));

            List<Serie> result = serieRepository.searchByTitle("Dark");

            assertEquals(1, result.size());
            assertEquals("Dark", result.get(0).getTitle());
        }

        @Test
        void testSearch_WithGenre() {
            when(jdbcTemplate.query(eq("SELECT * FROM serie WHERE genre = ?"), any(RowMapper.class), eq("Thriller")))
                    .thenReturn(List.of(testSerie));

            List<Serie> result = serieRepository.search("Thriller");

            assertEquals(1, result.size());
            assertEquals("Thriller", result.get(0).getGenre());
        }

        @Test
        void testSearchWithoutGenre() {
            when(jdbcTemplate.query(eq("SELECT * FROM serie"), any(RowMapper.class)))
                    .thenReturn(List.of(testSerie));

            List<Serie> result = serieRepository.search(null);

            assertEquals(1, result.size());
        }

        @Test
        void testSaveInsert() {
            Serie newSerie = new Serie(null, "Dark", "Thriller", 26, 8);

            when(jdbcTemplate.update(
                    eq("INSERT INTO serie (title, genre, nb_episode, note) VALUES (?, ?, ?, ?)"),
                    eq("Dark"), eq("Thriller"), eq(26), eq(8)))
                    .thenReturn(1);

            int result = serieRepository.save(newSerie);

            assertEquals(1, result);
        }

        @Test
        void testSaveUpdate() {
            when(jdbcTemplate.update(
                    eq("UPDATE serie SET title = ?, genre = ?, nb_episode = ?, note = ? WHERE id = ?"),
                    eq("Dark"), eq("Thriller"), eq(26), eq(8), eq(1)))
                    .thenReturn(1);

            int result = serieRepository.save(testSerie);

            assertEquals(1, result);
        }

        @Test
        void testDeleteById() {
            when(jdbcTemplate.update(eq("DELETE FROM serie WHERE id = ?"), eq(1)))
                    .thenReturn(1);

            int result = serieRepository.deleteById(1);

            assertEquals(1, result);
        }

        @Test
        void testExistsByIdTrue() {
            when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM serie WHERE ID= ?"), eq(Integer.class), eq(1)))
                    .thenReturn(1);

            boolean exists = serieRepository.existsById(1);

            assertTrue(exists);
        }

        @Test
        void testExistsByIdFalse() {
            when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM serie WHERE ID= ?"), eq(Integer.class), eq(999)))
                    .thenReturn(0);

            boolean exists = serieRepository.existsById(999);

            assertFalse(exists);
        }
    }


