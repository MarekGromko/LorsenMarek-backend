package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.dto.ErrorResponse;
import edu.lorsenmarek.backend.dto.RatingRequest;
import edu.lorsenmarek.backend.dto.RatingResponse;
import edu.lorsenmarek.backend.exception.RatingUnwatchedMediaException;
import edu.lorsenmarek.backend.exception.ResourceNotFoundException;
import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.service.SerieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rating/serie")
public class SerieRatingController {
    @Autowired
    SerieRatingService serieRatingService;
    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getSerieRating(
            @PathVariable("id") Long id
    ) {
        var mean = serieRatingService.getMeanRating(id);
        return ResponseEntity.ok(new RatingResponse(mean.sum(), mean.count()));
    }
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveSerieRating(
            @PathVariable("id") Long serieId,
            @RequestBody RatingRequest ratingReq,
            Authentication auth
    ) {
        var user = (User)(auth.getPrincipal());
        serieRatingService.tryRating(
                user.getId(),
                serieId,
                ratingReq.rating()
        );
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteSerieRating(
            @PathVariable("id") Long serieId,
            Authentication auth
    ) {
        var user = (User)(auth.getPrincipal());
        serieRatingService.deleteRating(
                user.getId(),
                serieId
        );
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RatingUnwatchedMediaException.class)
    public ResponseEntity<ErrorResponse> ratingUnwatchedMedia() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new ErrorResponse("RatingUnwatchedMedia", "Can not rate a serie that is not yet watched")
        );
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> serieNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse("ResourceNotFound", "Could not found serie %s".formatted(ex.getResourceId()))
        );
    }
}
