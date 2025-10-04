package edu.lorsenmarek.backend.model;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Serie {
    @Id
    private Long id;
    private String title;
    private Instant releaseTs;
}

