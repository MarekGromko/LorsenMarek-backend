package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.model.Person;
import edu.lorsenmarek.backend.repository.PersonRepository;
import edu.lorsenmarek.backend.utility.JSONContext;
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

    @


}
