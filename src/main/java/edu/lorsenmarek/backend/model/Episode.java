package edu.lorsenmarek.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * Model describing an Episode
 */
@Data
@AllArgsConstructor
@Builder
public class Episode {
    /** Create a new {@link Episode} */
    public Episode(){}
    @Id
    private Long id;
    private Long serieId;
    private Integer seasonNb;
    private String title;
    private Integer duration;
    private Instant releasedAt;
}
