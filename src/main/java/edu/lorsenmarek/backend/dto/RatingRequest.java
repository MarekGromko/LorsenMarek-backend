package edu.lorsenmarek.backend.dto;

/**
 * Request DTO for rating a media
 * <br>
 * <b>Example:</b>
 * <pre>{@code
 *  {
 *      "rating": 4
 *  }
 * }</pre>
 *
 *
 * @param rating the requested rating
 */
public record RatingRequest(Integer rating) {
}
