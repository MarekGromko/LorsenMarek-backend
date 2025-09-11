package edu.lorsenmarek.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import edu.lorsenmarek.backend.model.Person;
import edu.lorsenmarek.backend.repository.PersonRepository;
import edu.lorsenmarek.backend.utility.JSONContext;
import edu.lorsenmarek.backend.utility.PageOptions;
import edu.lorsenmarek.backend.utility.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonRepository personRepo;
    public PersonController(final PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @PostMapping("/person")
    public void addPerson(HttpServletRequest request, HttpServletResponse _response) {
        var response = new ResponseWrapper(_response);
        Person personToAdd;

        try {
            personToAdd = Person.fromJson(JSONContext.parseReader(request.getReader()));
            personToAdd.setId(null);
            response.initWriter();
        } catch (Exception e) {
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).flush();
            return;
        }

        personRepo.save(personToAdd);
        response.status(HttpStatus.CREATED).flush();
    }

    @PatchMapping("/person/{id}")
    public void patchPerson(
            @PathVariable("id") Integer personId,
            HttpServletRequest request,
            HttpServletResponse _response) {
        var response = new ResponseWrapper(_response);
        Person personToPatch;

        try {
            personToPatch = Person.fromJson(JSONContext.parseReader(request.getReader()));
            response.initWriter();
        } catch (Exception e) {
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).flush();
            return;
        }

        personToPatch.setId(personId);
        personRepo.save(personToPatch);
        response.status(HttpStatus.OK).flush();
    }

    @DeleteMapping("/person/id")
    public void deletePerson(
            @PathVariable("id") Integer personId,
            HttpServletResponse _response) {
        var response = new ResponseWrapper(_response);
        personRepo.deleteById(personId);
        response.status(HttpStatus.OK).flush();
    }

    @GetMapping("/search")
    public void searchPeople(
        @RequestParam(name = "name")     String name,
        @RequestParam(name = "baseId", required = false)   Integer baseId,
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        HttpServletResponse _response) {
        var response = new ResponseWrapper(_response);
        try {
            response.initWriter();
        } catch (Exception e) {
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).flush();
            return;
        }
        var opt = PageOptions.builder()
                        .pageSize(pageSize)
                        .baseId(baseId)
                        .build();
        var result = personRepo.searchByName(name, opt);

    }

}
