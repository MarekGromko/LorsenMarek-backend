package edu.lorsenmarek.backend.model;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSerieRating {
    private Long userId;
    private Long serieId;
    private Instant createdAt;
    private Instant modifiedAt;
    private Integer rating;
}
