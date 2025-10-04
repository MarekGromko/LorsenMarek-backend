package edu.lorsenmarek.backend.repositories;

import edu.lorsenmarek.backend.commons.options.SerieSearchOptions;
import edu.lorsenmarek.backend.models.Serie;

import java.util.List;

public interface SerieRepositoryCustom {
    List<Serie> searchByOptions(SerieSearchOptions options);
}
