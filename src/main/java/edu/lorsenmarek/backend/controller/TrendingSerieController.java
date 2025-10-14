package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.dto.SerieSummaryResponse;
import edu.lorsenmarek.backend.dto.SerieWithTrendingScoreResponse;
import edu.lorsenmarek.backend.service.TrendingSerieService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * REST controller managing trending functionalities for {@link edu.lorsenmarek.backend.model.Serie}
 *
 * @author Marek Gromko
 */
@Controller
@RequestMapping("/serie/trending")
public class TrendingSerieController {
    @Autowired
    TrendingSerieService trendingSerieService;
    @GetMapping
    @PermitAll
    public ResponseEntity<List<SerieWithTrendingScoreResponse>> GetTrendingSerie() {
        var results = trendingSerieService.getTrendingSeries();
        var response = results.stream().map(sts->{
            var serieSummary = new SerieSummaryResponse(
                    sts.serie().getId(),
                    sts.serie().getTitle(),
                    sts.serie().getReleasedAt()
            );
            return new SerieWithTrendingScoreResponse(
                    serieSummary,
                    sts.score()
            );
        }).toList();
        return ResponseEntity.ok(response);
    }
}
