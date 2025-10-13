package edu.lorsenmarek.backend.dto;

/**
 * Request DTO for rating a media
 * <p><br>
 *     <b>Example:</b>
 *     <pre>{@code
 *     {
 *         "rating": 4
 *     }
 *     }</pre>
 * </p>
 *
 * @param rating
 */
public record RatingRequest(Integer rating) {
}
