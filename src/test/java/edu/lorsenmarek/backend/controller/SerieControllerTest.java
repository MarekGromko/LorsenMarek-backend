package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.repository.SerieRepository;
import edu.lorsenmarek.backend.common.SerieSearchOption;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SerieController.class)
public class SerieControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SerieRepository serieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllSeries() throws Exception {
        Serie serie1 = new Serie(1, "Charmed", "Fantastique", 50, 10);
        Serie serie2 = new Serie(2, "Breacking Bad", "Drame", 62, 6);

        when(serieRepository.findAll()).thenReturn(Arrays.asList(serie1, serie2));

        mockMvc.perform(get("/serie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));


    }

    @Test
    void testGetSeriesByIdFound() throws Exception {
        Serie serie = new Serie(2, "Breaking Bad", "Drama", 62, 6);
        when(serieRepository.findById(2)).thenReturn(Optional.of(serie));

        mockMvc.perform(get("/serie/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Breaking Bad"));
    }
    @Test
    void testGetSeriesByIdNoFound() throws Exception{
        when(serieRepository.findById(2)).thenReturn(Optional.empty());
        mockMvc.perform(get("/serie/2")).andExpect(status().isNotFound());
    }

    @Test
    void testCreateSerie() throws Exception {
        Serie newSerie = new Serie(5, "Dark", "Thriller", 26, 8);
        when(serieRepository.save(any(Serie.class))).thenReturn(123);

        mockMvc.perform(post("/serie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSerie)))
                .andExpect(status().isOk())
                .andExpect(content().string("123"));
    }
    @Test
    void testUpdateSerieFound() throws Exception {
        Serie existing = new Serie(1, "Old Title", "Drama", 50, 8);
        Serie updated = new Serie(1, "New Title", "Comedy", 60, 9);

        when(serieRepository.findById(1)).thenReturn(Optional.of(existing));
        when(serieRepository.save(any(Serie.class))).thenReturn(1);

        mockMvc.perform(put("/serie/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void testUpdateSerieNotFound() throws Exception {
        Serie updated = new Serie(1, "New Title", "Comedy", 60, 9);
        when(serieRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/serie/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSerieFound() throws Exception {
        when(serieRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/serie/1"))
                .andExpect(status().isNoContent());

        verify(serieRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteSerieNotFound() throws Exception {
        when(serieRepository.existsById(1)).thenReturn(false);

        mockMvc.perform(delete("/serie/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchSeries_withParameters() throws Exception {
        Serie serie = new Serie();
        serie.setId(1);
        serie.setTitle("Dark");
        serie.setGenre("Thriller");
        serie.setNb_episode(20);
        serie.setNote(8);

        List<Serie> mockResult = List.of(serie);

        when(serieRepository.searchByOption(any(SerieSearchOption.class))).thenReturn(mockResult);
        mockMvc.perform(get("/serie/search")
                        .param("title", "Dark")
                        .param("genre", "Thriller")
                        .param("minEpisode", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Dark"))
                .andExpect(jsonPath("$[0].genre").value("Thriller"))
                .andExpect(jsonPath("$[0].nb_episode").value(20))
                .andExpect(jsonPath("$[0].note").value(8));

        ArgumentCaptor<SerieSearchOption> optionCaptor = ArgumentCaptor.forClass(SerieSearchOption.class);
        verify(serieRepository).searchByOption(optionCaptor.capture());

        SerieSearchOption capturedOption = optionCaptor.getValue();
        assert capturedOption.getTitle().equals("Dark");
        assert capturedOption.getGenre().equals("Thriller");
        assert capturedOption.getMinEpisode().equals(10);
    }

    @Test
    public void testSearchSeries_withoutParameters() throws Exception {

        when(serieRepository.searchByOption(any(SerieSearchOption.class))).thenReturn(List.of());


        mockMvc.perform(get("/serie/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(serieRepository).searchByOption(any(SerieSearchOption.class));
    }


}
