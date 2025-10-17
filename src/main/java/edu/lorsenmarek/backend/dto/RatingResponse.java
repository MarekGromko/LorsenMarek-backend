package edu.lorsenmarek.backend.dto;

/**
 * Response DTO after requesting a media rating
 * <p><br>
 *     <b>Example:</b>
 *     <pre>{@code
 *     {
 *         "sum": 4,
 *         "count": 2
 *     }
 *     }</pre>
 * </p>
 *
 * @param sum sum of all ratings found
 * @param count number of ratings found
 */
public record RatingResponse(Double sum, Double count) {
}
