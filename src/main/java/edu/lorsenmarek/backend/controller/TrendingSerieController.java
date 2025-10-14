package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.dto.*;
import edu.lorsenmarek.backend.service.TrendingSerieService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller managing trending functionalities for {@link edu.lorsenmarek.backend.model.Serie}
 *
 * @see TrendingSerieService
 * @see SerieAndScoreResponse
 * @author Marek Gromko
 */
@Controller
@PermitAll
@RequestMapping("/public/trending/serie")
public class TrendingSerieController {
    @Autowired
    TrendingSerieService trendingSerieService;
    /**
     * Get the mean rating (sum & count) for a given serie
     * <p><b>This route is permitted for all</b></p>
     * <p><br/>
     *     <b>Example: </b>
     *     <pre>{@code
     *      // Request
     *      GET /trending/series
     *      // Response
     *      HTTP 200 Ok
     *      [
     *          {
     *              "score": 12.0,
     *              "serie": {
     *                  "id": 1,
     *                  "title": "Some Title",
     *                  "releasedAt": "2020-12-32 03:45:20"
     *              }
     *          },
     *          ...
     *      ]
     *     }</pre>
     * </p>
     *
     * @return Ok with a {@link List} of {@link SerieAndScoreResponse} for body
     */
    @GetMapping
    public ResponseEntity<List<SerieAndScoreResponse>> getTrendingSerie() {
        var results = trendingSerieService.getTrendingSeries();
        var response = results.stream()
                .map(sts->new SerieAndScoreResponse(sts.serie(), sts.score()))
                .toList();
        return ResponseEntity.ok(response);
    }
}
