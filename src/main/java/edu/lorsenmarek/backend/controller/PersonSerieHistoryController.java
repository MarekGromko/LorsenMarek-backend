package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.model.PersonSerieHistory;
import edu.lorsenmarek.backend.repository.PersonSerieHistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/person-serie-history")
public class PersonSerieHistoryController {
    private final PersonSerieHistoryRepository PSHRepo;
    public PersonSerieHistoryController(final PersonSerieHistoryRepository PSHRepo) {
        this.PSHRepo = PSHRepo;
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<PersonSerieHistory>> getPersonHistory(
            @PathVariable("id") Integer id
    ) {
        var result = PSHRepo.findByPerson(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("")
    public ResponseEntity<?> saveHistory(@RequestBody PersonSerieHistory PSHToAdd) {
        PSHRepo.save(PSHToAdd);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{personId}/{serieId}")
    public ResponseEntity<?> deleteHistory(
            @PathVariable(value = "personId") Integer personId,
            @PathVariable(value = "serieId") Integer serieId
    ){
        if(PSHRepo.deleteByIds(personId, serieId) == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
