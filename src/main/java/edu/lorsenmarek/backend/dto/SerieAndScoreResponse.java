package edu.lorsenmarek.backend.dto;

import edu.lorsenmarek.backend.model.Serie;

/**
 * A response DTO which combine a {@link SerieSummaryResponse} with a score
 * <p><br>
 *     <b>Example:</b>
 *     <pre>{@code
 *      {
 *          "score": 5.0,
 *          "serie": {
 *              ... // SerieSummaryResponse
 *          }
 *      }
 *     }</pre>
 * </p>
 *
 * @see SerieSummaryResponse
 * @see Serie
 * @param serie a {@link SerieSummaryResponse}
 * @param score the serie score
 * @author Marek Gromko
 */
public record SerieAndScoreResponse(
        SerieSummaryResponse serie,
        Double score
){
    /**
     * Populate the {@link SerieSummaryResponse} from a {@link Serie}
     *
     * @param serie the {@link Serie} to populate the {@link SerieSummaryResponse}
     * @param score the serie score
     */
    public SerieAndScoreResponse(Serie serie, Double score) {
        this(new SerieSummaryResponse(serie), score);
    }
}
