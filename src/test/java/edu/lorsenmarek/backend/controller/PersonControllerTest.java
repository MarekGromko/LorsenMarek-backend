package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lorsenmarek.backend.model.Person;
import edu.lorsenmarek.backend.repository.PersonRepository;
import edu.lorsenmarek.backend.common.PageOptions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {
    final Person JaneDoe = Person.builder()
            .id(5)
            .firstName("Jane")
            .lastName("Doe")
            .email("janeDoe@abc.def")
            .gender("F")
            .build();
    final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PersonRepository mockPersonRepo;
    @Nested
    class getAllPeople{
        @Test
        void whenNoPage_pageOptionsShouldBeNull() throws Exception {
            // arrange
            when(mockPersonRepo.findAll(null)).thenReturn(List.of(JaneDoe));

            // act
            var result = mockMvc.perform(get("/person/all"));

            // assert
            result.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].firstName").value("Jane"));
            verify(mockPersonRepo).findAll(null);
        }
        @Test
        void whenPage_pageOptionsShouldBeSet() throws Exception {
            // arrange
            when(mockPersonRepo.findAll(any(PageOptions.class))).thenReturn((List.of(JaneDoe)));

            // act
            var result = mockMvc.perform(get("/person/all")
                    .param("pageIndex", "1")
                    .param("pageSize", "10"));

            // assert
            result.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].firstName").value("Jane"));
            verify(mockPersonRepo).findAll(any(PageOptions.class));
        }
    }
    @Test
    void getPerson_shouldCallRepoFindById() throws Exception {
        // arrange
        when(mockPersonRepo.findById(anyInt())).thenReturn(Optional.of(JaneDoe));

        // act
        var result = mockMvc.perform(get("/person/10"));

        // assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value("Jane"));
        verify(mockPersonRepo).findById(anyInt());
    }
    @Nested
    class addPerson {
        @Test
        void whenInsertSuccess_statusShouldBeCreated() throws Exception{
            // arrange
            when(mockPersonRepo.insert(any(Person.class))).thenReturn(1);

            // act
            var result = mockMvc.perform(post("/person")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(JaneDoe)));

            // assert
            result.andExpect(status().isCreated());
            verify(mockPersonRepo).insert(any(Person.class));
        }
        @Test
        void whenInsertFail_statusShouldBeNotAccepted() throws Exception {
            // arrange
            when(mockPersonRepo.insert(any(Person.class))).thenReturn(0);

            // act
            var result = mockMvc.perform(post("/person")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(JaneDoe)));

            // assert
            result.andExpect(status().isNotAcceptable());
            verify(mockPersonRepo).insert(any(Person.class));
        }
    }
    @Nested
    class patchPerson{
        @Test
        void whenUpdateSuccess_statusShouldBeOk() throws Exception {
            // arrange
            when(mockPersonRepo.update(any(Person.class))).thenReturn(1);

            // act
            var result = mockMvc.perform(patch("/person/10")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(JaneDoe)));

            // assert
            result.andExpect(status().isOk());
            verify(mockPersonRepo).update(any(Person.class));
        }
        @Test
        void whenUpdateFail_statusShouldBeNotFound() throws Exception {
            // arrange
            when(mockPersonRepo.update(any(Person.class))).thenReturn(0);

            // act
            var result = mockMvc.perform(patch("/person/10")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(JaneDoe)));

            // assert
            result.andExpect(status().isNotFound());
            verify(mockPersonRepo).update(any(Person.class));
        }
    }
    @Nested
    class deletePerson{
        @Test
        void whenDeleteSuccess_statusShouldBeOk() throws Exception {
            // arrange
            when(mockPersonRepo.deleteById(anyInt())).thenReturn(1);

            // act
            var result = mockMvc.perform(delete("/person/10"));

            // assert
            result.andExpect(status().isOk());
            verify(mockPersonRepo).deleteById(10);
        }
        @Test
        void whenInsertFail_statusShouldBeNotFound() throws Exception {
            // arrange
            when(mockPersonRepo.deleteById(anyInt())).thenReturn(0);

            // act
            var result = mockMvc.perform(delete("/person/12"));

            // assert
            result.andExpect(status().isNotFound());
            verify(mockPersonRepo).deleteById(12);
        }
    }
    @Nested
    class searchPeople{
        @Test
        void whenNoPageParams_PageOptionsShouldBeNull() throws Exception {
            // arrange
            when(mockPersonRepo.searchByName(anyString(), any(PageOptions.class))).thenReturn(List.of(JaneDoe));

            // act
            var result = mockMvc.perform(get("/person/search")
                    .param("name", "Jane"));

            // assert
            result.andExpect(status().isOk());
            verify(mockPersonRepo).searchByName("Jane", null);
        }
        @Test
        void whenPageParams_PageOptionsShouldNotBeNull() throws Exception {
            // arrange
            when(mockPersonRepo.searchByName(anyString(), any(PageOptions.class))).thenReturn(List.of(JaneDoe));

            // act
            var result = mockMvc.perform(get("/person/search")
                    .param("name", "Jane")
                    .param("pageSize", "10")
                    .param("pageIndex", "1"));

            // assert
            result.andExpect(status().isOk());
            verify(mockPersonRepo).searchByName(anyString(), any(PageOptions.class));
        }
    }
}