package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import edu.lorsenmarek.backend.model.Person;
import edu.lorsenmarek.backend.repository.PersonRepository;
import edu.lorsenmarek.backend.utility.PageOptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonRepository personRepo;
    public PersonController(final PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Person>> getAllPerson(
            @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(name = "pageSize",  required = false) Integer pageSize) {
        PageOptions pageOpts = null;
        if(pageIndex != null && pageSize != null) {
            pageOpts = PageOptions.builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .build();
        }
        var result = personRepo.findAll(pageOpts);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable("id") Integer id) {
        var result = personRepo.findById(id);
        return ResponseEntity.of(result);
    }
    @PostMapping("/")
    public ResponseEntity<?> addPerson(@RequestBody Person personToAdd) {
        personRepo.save(personToAdd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchPerson(
            @PathVariable("id") Integer personId,
            @RequestBody Person personToPatch) {

        personToPatch.setId(personId);
        personRepo.save(personToPatch);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(
            @PathVariable("id") Integer personId,
            @RequestBody Person personToPatch) {
        personRepo.deleteById(personId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPeople(
        @RequestParam(name = "name") String name,
        @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
        @RequestParam(name = "pageSize",  required = false) Integer pageSize) {
        PageOptions pageOpts = null;
        if(pageIndex != null && pageSize != null) {
            pageOpts = PageOptions.builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .build();
        }
        var result = personRepo.searchByName(name, pageOpts);
        return ResponseEntity.ok(result);

    }

}
