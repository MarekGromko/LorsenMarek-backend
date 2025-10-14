package edu.lorsenmarek.backend.dto;

import edu.lorsenmarek.backend.model.Serie;

import java.time.Instant;

/**
 * Response DTO holding summary information about a {@link Serie}
 * <p><br>
 *     <b>Example:</b>
 *     <pre>{@code
 *     {
 *         "id": 1,
 *         "title": "Some Serie",
 *         "releasedAt": "2007-12-12 5:30:10"
 *     }
 *     }</pre>
 * </p>
 *
 * @see Serie
 * @param id the serie id
 * @param title the serie title
 * @param releasedAt the released date of the serie
 * @author Marek Gromko
 */
public record SerieSummaryResponse(
        Long id,
        String title,
        Instant releasedAt
) {
    /**
     * Create a {@link SerieSummaryResponse} from a {@link Serie}
     *
     * @param serie the {@link Serie} to populate the {@link SerieSummaryResponse}
     */
    public SerieSummaryResponse(final Serie serie) {
        this(serie.getId(), serie.getTitle(), serie.getReleasedAt());
    }
}
