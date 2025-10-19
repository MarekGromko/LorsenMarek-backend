package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.common.option.SerieSearchOptions;
import edu.lorsenmarek.backend.model.Serie;

import java.util.List;

/**
 * Custom repository interface for advanced {@link Serie} search operations.
 *  @author Marek Gromko
 */
public interface SerieRepositoryCustom {
    /**
     * Search a serie by option
     * @param options the search options
     * @return a {@link List} of {@link Serie} matching the options provided
     */
    List<Serie> searchByOptions(SerieSearchOptions options);
}
