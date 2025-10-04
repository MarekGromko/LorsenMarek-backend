package edu.lorsenmarek.backend.repositories;

import edu.lorsenmarek.backend.commons.SerieSearchOption;
import edu.lorsenmarek.backend.models.Serie;

import java.util.List;

public interface SerieRepositoryCustom {
    List<Serie> searchByOption(SerieSearchOption options);
}
