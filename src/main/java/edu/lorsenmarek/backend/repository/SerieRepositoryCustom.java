package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.common.option.SerieSearchOptions;
import edu.lorsenmarek.backend.model.Serie;

import java.util.List;

/**
 * Custom repository interface for advanced {@link Serie} search operations.
 * @author Lorsen Lamour
 * @version 1.0
 */
public interface SerieRepositoryCustom {
    List<Serie> searchByOptions(SerieSearchOptions options);
}
