package edu.lorsenmarek.backend.model;

import java.time.Instant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEpisodeHistory {
    private Long userId;
    private Long episodeId;
    private Instant watchedAt;
    private Integer watchedDuration;
}
