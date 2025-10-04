package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.common.option.SerieSearchOptions;
import edu.lorsenmarek.backend.model.Serie;

import java.util.List;

public interface SerieRepositoryCustom {
    List<Serie> searchByOptions(SerieSearchOptions options);
}
