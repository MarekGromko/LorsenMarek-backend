package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.repository.SerieRepository;
import org.junit.jupiter.api.Test;
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
    void testSearchByGenre() throws Exception {
        Serie serie = new Serie(5, "Dark", "Thriller", 26, 8);
        when(serieRepository.search("Thriller")).thenReturn(List.of(serie));

        mockMvc.perform(get("/serie/search").param("genre", "Thriller"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].genre").value("Thriller"));
    }

    @Test
    void testSearchByTitle() throws Exception {
        Serie serie = new Serie(5, "Dark", "Thriller", 26, 8);
        when(serieRepository.searchByTitle("Dark")).thenReturn(List.of(serie));

        mockMvc.perform(get("/serie/searchByTitle").param("title", "Dark"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Dark"));
    }

}
