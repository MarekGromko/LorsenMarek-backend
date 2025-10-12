package edu.lorsenmarek.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Season {
    @Id
    private Long id;
    private Long serieId;
    private String title;
    private Instant releasedAt;
}
