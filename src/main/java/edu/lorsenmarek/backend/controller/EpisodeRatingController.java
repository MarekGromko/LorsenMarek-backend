package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.dto.*;
import edu.lorsenmarek.backend.exception.*;
import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.service.EpisodeRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rating/episode")
public class EpisodeRatingController {
    @Autowired
    EpisodeRatingService episodeRatingService;
    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getEpisodeRating(
            @PathVariable("id") Long id
    ) {
        var mean = episodeRatingService.getMeanRating(id);
        return ResponseEntity.ok(new RatingResponse(mean.sum(), mean.count()));
    }
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveEpisodeRating(
            @PathVariable("id") Long episodeId,
            @RequestBody RatingRequest ratingReq,
            Authentication auth
    ) {
        var user = (User)(auth.getPrincipal());
        episodeRatingService.tryRating(
                user.getId(),
                episodeId,
                ratingReq.rating()
        );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteEpisodeRating(
            @PathVariable("id") Long episodeId,
            Authentication auth
    ) {
        var user = (User)(auth.getPrincipal());
        episodeRatingService.deleteRating(
                user.getId(),
                episodeId
        );
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(RatingUnseenMediaException.class)
    public ResponseEntity<ErrorResponse> ratingUnseenMedia() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse("RatingUnseenMedia", "Can not rate an episode that isn't already watch")
        );
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> episodeNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse("ResourceNotFound", "Could not found episode %s".formatted(ex.getResourceId()))
        );
    }
}
