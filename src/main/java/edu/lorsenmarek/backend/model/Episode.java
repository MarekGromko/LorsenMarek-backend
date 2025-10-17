package edu.lorsenmarek.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * Model describing an Episode
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Episode {
    @Id
    private Long id;
    private Long serieId;
    private Integer seasonNb;
    private String title;
    private Integer duration;
    private Instant releasedAt;
}
