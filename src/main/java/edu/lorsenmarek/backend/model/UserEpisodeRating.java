package edu.lorsenmarek.backend.model;

import edu.lorsenmarek.backend.annotation.CompositeId;
import lombok.*;

import java.time.Instant;

/**
 * Model describing a rating to an {@link Episode} by a {@link User}
 */
@Data
@AllArgsConstructor
@Builder
public class UserEpisodeRating {
    /** Create a new {@link UserEpisodeRating} */
    public UserEpisodeRating() {}
    @CompositeId
    private Long userId;
    @CompositeId
    private Long episodeId;
    private Instant createdAt;
    private Instant modifiedAt;
    private Integer rating;
}
