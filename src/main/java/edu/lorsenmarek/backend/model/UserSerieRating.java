package edu.lorsenmarek.backend.model;

import edu.lorsenmarek.backend.annotation.CompositeId;
import lombok.*;

import java.time.Instant;

/**
 * Model describing a rating to a {@link Serie} by a {@link User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSerieRating {
    @CompositeId
    private Long userId;
    @CompositeId
    private Long serieId;
    private Instant createdAt;
    private Instant modifiedAt;
    private Integer rating;
}
