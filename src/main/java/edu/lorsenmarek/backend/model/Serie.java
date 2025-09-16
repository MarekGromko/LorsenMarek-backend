package edu.lorsenmarek.backend.model;
import lombok.*;

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

