package edu.lorsenmarek.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonSerieHistory {
    private Integer personId;
    private Integer serieId;
    private Instant lastWatch;
    private Integer watchDurationMs;
}
