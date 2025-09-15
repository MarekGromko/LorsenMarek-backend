package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.model.Serie;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import edu.lorsenmarek.backend.repository.SeriesRepository;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;

    public SeriesService(SeriesRepository seriesRepository) {

        this.seriesRepository = seriesRepository;
    }

    public int save(Serie newSerie) {
        return seriesRepository.save(newSerie);

    }

    public  Optional<Serie> findById(int id) {
        return seriesRepository.findById(id);
    }

    /**
     * Recherche les séries par genre.
     * Si genre est null ou vide, retourne toutes les séries.
     */
    public List<Serie> search(String genre) {
        if (genre == null || genre.isEmpty()) {
            return seriesRepository.findAll();
        } else {
            return seriesRepository.findByGenre(genre);
        }
    }

    public List<Serie> findAll() {
        return seriesRepository.findAll();
    }

    public boolean existsById(int id) {
        return seriesRepository.existsById(id);
    }

    public void deleteById(int id) {
        seriesRepository.existsById(id);
    }
}