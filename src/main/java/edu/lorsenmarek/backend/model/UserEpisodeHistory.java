package edu.lorsenmarek.backend.model;

import java.time.Instant;

import edu.lorsenmarek.backend.annotation.CompositeId;
import lombok.*;

/**
 * Model describing an {@link Episode} watched by a {@link User}
 */
@Data
@AllArgsConstructor
@Builder
public class UserEpisodeHistory {
    /** Create a new {@link UserEpisodeHistory} */
    public UserEpisodeHistory() {}
    @CompositeId
    private Long userId;
    @CompositeId
    private Long episodeId;
    private Instant watchedAt;
    private Integer watchedDuration;
}
