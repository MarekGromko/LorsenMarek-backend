package edu.lorsenmarek.backend.controller;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.repository.SerieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

public class SerieController {
    private final SerieRepository serieRepository;

    public SerieController(SerieRepository serieRepository){
        this.serieRepository = serieRepository;
    }


    @GetMapping
    public List<Serie> getAllSerie() {
        return serieRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Serie> getSeriesById(@PathVariable int id) {
        Optional<Serie> serie = serieRepository.findById(id);
        return serie.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public Serie createSeries(@RequestBody Serie newSerie) {
        return SerieRepository.save(newSerie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Serie> updateSeries(@PathVariable int id, @RequestBody Serie updatedSeries) {
        return SerieRepository.findById(id).map(serie -> {
            serie.setTitre(updatedSeries.getTitre());
            serie.setGenre(updatedSeries.getGenre());
            serie.setNbEpisodes(updatedSeries.getNbEpisodes());
            serie.setNote(updatedSeries.getNote());
            repository.save(serie);
            return ResponseEntity.ok(serie);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    // DELETE /series/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable int id) {
        if (serieRepository.existsById(id)) {
            serieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    }
