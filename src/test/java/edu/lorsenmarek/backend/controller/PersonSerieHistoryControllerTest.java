package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.model.PersonSerieHistory;
import edu.lorsenmarek.backend.repository.PersonSerieHistoryRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonSerieHistoryController.class)
class PersonSerieHistoryControllerTest {
    final PersonSerieHistory PSHStub = PersonSerieHistory.builder()
            .lastWatch(Instant.now())
            .serieId(2)
            .personId(1)
            .instanceWatch(5)
            .build();

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonSerieHistoryRepository mockPSHRepo;
    @Test
    void getPersonHistory_shouldCallRepoFindByPerson() throws Exception{
        // arrange
        when(mockPSHRepo.findByPerson(anyInt())).thenReturn(List.of(PSHStub));

        // act
        var result = mockMvc.perform(get("/person-serie-history/1"));

        // assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].personId").value("1"));
        verify(mockPSHRepo).findByPerson(1);
    }
    @Test
    void saveHistory_shouldCallRepoSave() throws Exception {
        // act
        var result = mockMvc.perform(post("/person-serie-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(PSHStub)));

        // capture
        ArgumentCaptor<PersonSerieHistory> pshCaptor = ArgumentCaptor.forClass(PersonSerieHistory.class);
        verify(mockPSHRepo).save(pshCaptor.capture());

        // assert
        result.andExpect(status().isOk());
        verify(mockPSHRepo).save(any(PersonSerieHistory.class));
        assertEquals(PSHStub.getLastWatch(), pshCaptor.getValue().getLastWatch());
    }
    @Nested
    class deleteHistory {
        @Test
        void whenDeleteFail_shouldRespondNotFound() throws Exception{
            // arrange
            when(mockPSHRepo.deleteByIds(anyInt(), anyInt())).thenReturn(0);

            // act
            var result = mockMvc.perform(delete("/person-serie-history/1/2"));

            // assert
            result.andExpect(status().isNotFound());
            verify(mockPSHRepo).deleteByIds(1,2);
        }
        @Test
        void whenDeleteSucceed_shouldRespondOk() throws Exception{
            // arrange
            when(mockPSHRepo.deleteByIds(anyInt(), anyInt())).thenReturn(1);

            // act
            var result = mockMvc.perform(delete("/person-serie-history/1/2"));

            // assert
            result.andExpect(status().isOk());
            verify(mockPSHRepo).deleteByIds(1,2);
        }
    }
}