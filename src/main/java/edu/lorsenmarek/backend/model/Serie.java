package edu.lorsenmarek.backend.model;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * Model describing an Serie
 */
@Data
@AllArgsConstructor
@Builder
public class Serie {
    /** Create a new {@link Serie} */
    public Serie(){}
    @Id
    private Long id;
    private String title;
    private Instant releasedAt;
}

