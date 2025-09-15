package com.tonprojet.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.tonprojet.model.Serie;
import com.tonprojet.repository.SeriesRepository;

@Service
public class SeriesService {

    private final SerieRepository seriesRepository;

    public SeriesService(SerieRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    /**
     * Recherche les séries par genre.
     * Si genre est null ou vide, retourne toutes les séries.
     */
    public List<Series> search(String genre) {
        if (genre == null || genre.isEmpty()) {
            return seriesRepository.findAll();
        } else {
            return seriesRepository.findByGenre(genre);
        }
    }
}