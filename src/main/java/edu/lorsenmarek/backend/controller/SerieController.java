package edu.lorsenmarek.backend.controller;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.repository.SerieRepository;
import edu.lorsenmarek.backend.utility.SerieSearchOption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/serie")
public class SerieController {
    private final SerieRepository serieRepository;

    public SerieController(SerieRepository serieRepository){
        this.serieRepository = serieRepository;
    }

    @GetMapping("/search")
    public List<Serie> searchSeries(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer minEpisode
    ) {
        SerieSearchOption option = new SerieSearchOption();
        option.setTitle(title);
        option.setGenre(genre);
        option.setMinEpisode(minEpisode);
        return serieRepository.searchByOption(option);
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
    public int createSerie(@RequestBody Serie newSerie) {
        return serieRepository.save(newSerie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Serie> updateSerie(@PathVariable int id, @RequestBody Serie updatedSerie) {
        return serieRepository.findById(id).map(serie -> {
            serie.setTitle(updatedSerie.getTitle());
            serie.setGenre(updatedSerie.getGenre());
            serie.setNb_episode(updatedSerie.getNb_episode());
            serie.setNote(updatedSerie.getNote());
            serieRepository.save(serie);
            return ResponseEntity.ok(serie);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerie(@PathVariable int id) {
        if (serieRepository.existsById(id)) {
            serieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    }
