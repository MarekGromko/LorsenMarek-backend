package edu.lorsenmarek.backend.model;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Serie {
    private Integer id;
    private String title;
    private String genre;
    private Integer nb_episode;
    private Integer note;
}

