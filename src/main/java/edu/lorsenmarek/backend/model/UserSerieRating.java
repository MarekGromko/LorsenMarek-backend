package edu.lorsenmarek.backend.model;

import edu.lorsenmarek.backend.annotation.CompositeId;
import edu.lorsenmarek.backend.repository.UserSerieRatingRepository;
import lombok.*;

import java.time.Instant;

/**
 * Model describing a rating to a {@link Serie} by a {@link User}
 */
@Data
@AllArgsConstructor
@Builder
public class UserSerieRating {
    /** Create a new {@link UserSerieRating} */
    public UserSerieRating(){}
    @CompositeId
    private Long userId;
    @CompositeId
    private Long serieId;
    private Instant createdAt;
    private Instant modifiedAt;
    private Integer rating;
}
