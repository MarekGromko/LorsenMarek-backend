package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.common.option.SerieSearchOptions;
import edu.lorsenmarek.backend.model.Serie;

import java.util.List;

/**
 * Custom repository interface for advanced {@link Serie} search operations.
 *  @author Marek Gromko
 */
public interface SerieRepositoryCustom {
    List<Serie> searchByOptions(SerieSearchOptions options);
}
