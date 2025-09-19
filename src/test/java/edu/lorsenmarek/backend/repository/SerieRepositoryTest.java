package edu.lorsenmarek.backend.repository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.utility.SerieSearchOption;
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
    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @Captor
    private ArgumentCaptor<Object[]> paramsCaptor;
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
    public void testSearchByOption_withAllParameters() {

        SerieSearchOption option = new SerieSearchOption();
        option.setTitle("dark");
        option.setGenre("Thriller");
        option.setMinEpisode(10);

        List<Serie> expected = List.of(new Serie());
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(expected);


        List<Serie> result = serieRepository.searchByOption(option);


        assertNotNull(result);
        assertEquals(expected, result);

        verify(jdbcTemplate).query(sqlCaptor.capture(), paramsCaptor.capture(), any(RowMapper.class));

        String sql = sqlCaptor.getValue();
        Object[] params = paramsCaptor.getValue();


        assertTrue(sql.contains("LOWER(title) LIKE ?"));
        assertTrue(sql.contains("genre = ?"));
        assertTrue(sql.contains("nb_episode >= ?"));

        assertEquals("%dark%", params[0]);
        assertEquals("Thriller", params[1]);
        assertEquals(10, params[2]);
    }
    @Test
    public void testSearchByOption_withNoParameters() {

        SerieSearchOption option = new SerieSearchOption(); // tous null

        List<Serie> expected = List.of(new Serie());
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(expected);


        List<Serie> result = serieRepository.searchByOption(option);


        assertNotNull(result);
        assertEquals(expected, result);

        verify(jdbcTemplate).query(sqlCaptor.capture(), paramsCaptor.capture(), any(RowMapper.class));

        String sql = sqlCaptor.getValue();
        Object[] params = paramsCaptor.getValue();


        assertEquals("SELECT * FROM serie WHERE 1=1", sql);

        assertEquals(0, params.length);
    }

    @Test
    public void testSearchByOption_withPartialParameters() {

        SerieSearchOption option = new SerieSearchOption();
        option.setTitle("test");
        option.setMinEpisode(5);

        List<Serie> expected = List.of(new Serie());
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(expected);


        List<Serie> result = serieRepository.searchByOption(option);


        assertNotNull(result);
        assertEquals(expected, result);

        verify(jdbcTemplate).query(sqlCaptor.capture(), paramsCaptor.capture(), any(RowMapper.class));

        String sql = sqlCaptor.getValue();
        Object[] params = paramsCaptor.getValue();

        assertTrue(sql.contains("LOWER(title) LIKE ?"));
        assertTrue(sql.contains("nb_episode >= ?"));
        assertFalse(sql.contains("genre = ?"));

        assertEquals("%test%", params[0]);
        assertEquals(5, params[1]);
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


