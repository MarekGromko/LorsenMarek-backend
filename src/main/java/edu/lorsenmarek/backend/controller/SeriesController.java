package edu.lorsenmarek.backend.controller;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.service.SeriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/serie")
public class SeriesController {
    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService){
        this.seriesService = seriesService;
    }

@GetMapping("/search")
public List<Serie> searchSeries(@RequestParam(required = false)String genre){
        return seriesService.search(genre);
}
    @GetMapping
    public List<Serie> getAllSerie() {
        return seriesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Serie> getSeriesById(@PathVariable int id) {
        Optional<Serie> serie = seriesService.findById(id);
        return serie.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public int createSeries(@RequestBody Serie newSerie) {
        return seriesService.save(newSerie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Serie> updateSeries(@PathVariable int id, @RequestBody Serie updatedSeries) {
        return seriesService.findById(id).map(serie -> {
            serie.setTitle(updatedSeries.getTitle());
            serie.setGenre(updatedSeries.getGenre());
            serie.setNb_episode(updatedSeries.getNb_episode());
            serie.setNote(updatedSeries.getNote());
            seriesService.save(serie);
            return ResponseEntity.ok(serie);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    // DELETE /series/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable int id) {
        if (seriesService.existsById(id)) {
            seriesService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    }
