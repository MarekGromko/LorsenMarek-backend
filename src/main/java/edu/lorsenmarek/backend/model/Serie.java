package edu.lorsenmarek.backend.model;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Serie {
    @Id
    private Integer id;
    private String title;
    private String genre;
    private Integer nb_episode;
    private Integer note;
}

