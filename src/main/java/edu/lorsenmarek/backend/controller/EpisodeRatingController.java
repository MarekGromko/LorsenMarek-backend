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

/**
 * REST controller managing rating functionalities for {@link edu.lorsenmarek.backend.model.Episode}
 *
 * @author Marek Gromko
 */
@Controller
@RequestMapping("/rating/episode")
public class EpisodeRatingController {
    @Autowired
    EpisodeRatingService episodeRatingService;
    /**
     * Get the mean rating (sum & count) for a given episode
     * <p><br/>
     *     <b>Example:</b>
     *     <pre>{@code
     *      // Request
     *      GET /rating/episode/{id}
     *      // Response
     *      HTTP 200 Ok
     *      {
     *          "sum": 4, // sum of all ratings found
     *          "count": 2 // number of ratings found
     *      }
     *     }</pre>
     * </p>
     *
     * @param id the {@link edu.lorsenmarek.backend.model.Episode} id as a path variable
     * @return - Ok with {@link RatingResponse} for body
     * <p>
     *     - In case of error, this method may delegate to :
     *     <ul>
     *         <li>{@link #episodeNotFound(ResourceNotFoundException)}</li>
     *     </ul>
     * </p>
     */
    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getEpisodeRating(
            @PathVariable("id") Long id
    ) {
        var mean = episodeRatingService.getMeanRating(id);
        return ResponseEntity.ok(new RatingResponse(mean.sum(), mean.count()));
    }
    /**
     * Add or modify a rating to an {@link edu.lorsenmarek.backend.model.Episode}
     * <p>User must be <b>authenticated</b></p>
     * <p><br/>
     *     <b>Example:</b>
     *     <pre>{@code
     *     // Request
     *     PUT /rating/episode/{episodeId}
     *     {
     *         "rating": 4 // the rating to put
     *     }
     *     // Response
     *     HTTP 204 No Content
     *     }</pre>
     * </p>
     *
     * @param episodeId the {@link edu.lorsenmarek.backend.model.Episode} id as a path variable
     * @param ratingReq the {@link RatingRequest} as the request body
     * @param auth the {@link edu.lorsenmarek.backend.security.token.DetailedAuthToken}
     * @return - No Content
     * <p>
     *     - In case of error, this method may delegate to :
     *     <ul>
     *         <li>{@link #ratingUnwatchedMedia()}</li>
     *         <li>{@link #episodeNotFound(ResourceNotFoundException)}</li>
     *     </ul>
     * </p>
     */
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
    /**
     * Delete an episode rating
     * <p>User must be <b>authenticated</b></p>
     * <p><br/>
     *     <b>Example:</b>
     *     <pre>{@code
     *     // Request
     *     DELETE /rating/episode/{episodeId}
     *     // Response
     *     HTTP 204 No Content
     *     }</pre>
     * </p>
     *
     * @param episodeId the {@link edu.lorsenmarek.backend.model.Episode} as a path variable
     * @param auth the {@link edu.lorsenmarek.backend.security.token.DetailedAuthToken}
     * @return - No Content
     * <p>
     *     - In case of error, this method may delegate to :
     *     <ul>
     *         <li>{@link #episodeNotFound(ResourceNotFoundException)}</li>
     *     </ul>
     * </p>
     */
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
    /**
     * Deferred to when a request to rate is made to an episode not yet watched
     * <p>Handle exception {@link RatingUnwatchedMediaException}</p>
     * <p>Code field is <b>{@code RatingUnwatchedMedia}</b></p>
     * <p><br/>
     *     <b>Example:</b>
     *     <pre>{@code
     *     // Response
     *     HTTP 403 Forbidden
     *     {
     *         "code": "RatingUnwatchedMedia",
     *         "message": "Can't rate an episode that is not yet watched"
     *     }
     *     }</pre>
     * </p>
     *
     * @return Forbidden with {@link ErrorResponse} for body
     */
    @ExceptionHandler(RatingUnwatchedMediaException.class)
    public ResponseEntity<ErrorResponse> ratingUnwatchedMedia() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse("RatingUnwatchedMedia", "Can't rate an episode that is not yet watched")
        );
    }
    /**
     * Deferred to when a requested episode does not exist
     * <p>Handle exception {@link ResourceNotFoundException}</p>
     * <p>Code field is <b>{@code ResourceNotFound}</b></p>
     * <p><br/>
     *     <b>Example:</b>
     *     <pre>{@code
     *     // Response
     *     HTTP 404 Not Found
     *     {
     *         "code": "ResourceNotFound",
     *         "message": "Could not find episode {episodeId}"
     *     }
     *     }</pre>
     * </p>
     *
     * @param ex the {@link ResourceNotFoundException} caught
     * @return Forbidden with {@link ErrorResponse} for body
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> episodeNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse("ResourceNotFound", "Could not find episode %s".formatted(ex.getResourceId()))
        );
    }
}
