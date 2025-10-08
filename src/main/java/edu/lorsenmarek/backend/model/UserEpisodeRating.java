package edu.lorsenmarek.backend.model;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEpisodeRating {
    private Long userId;
    private Long episodeId;
    private Instant createdAt;
    private Instant modifiedAt;
    private Integer rating;
}
