package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.converter.jdbc.SerieRowMapper;
import edu.lorsenmarek.backend.model.Serie;
import edu.lorsenmarek.backend.util.DurationCodecUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Service managing {@link Serie} trending functionalities.
 * <p><br/>
 *     As trending series are <code>pure</code>, this service is a singleton that
 *     "caches" the current trending series, and will only fetch them once in a while
 * </p>
 * <p><br/>
 *     The
 * </p>
 *
 * @see Serie
 * @author Marek Gromko
 */
@Service
@Scope("singleton")
public class TrendingSerieService {
    /**
     * Object holding both a {@link Serie} and its trending {@link #score()}
     *
     * @param serie
     * @param score
     */
    public record SerieTrendingScore(Serie serie, Double score) {}
    public static class SerieTrendingScoreRowMapper implements RowMapper<SerieTrendingScore> {
        private final RowMapper<Serie> serieMapper = new SerieRowMapper();
        @Override
        @NonNull
        public SerieTrendingScore mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new SerieTrendingScore(
                    serieMapper.mapRow(rs, rowNum),
                    rs.getDouble("trending_score")
            );
        }
    }
    private final JdbcTemplate jdbc;
    /**
     * The interval before recomputing the trending serie
     *
     * @value default {@code PT10M}
     */
    private final Duration COMPUTE_INTERVAL;
    /**
     * Control how much the number of view
     * factor into the trending score
     *
     * @value default {@code 1.0}
     */
    private final Double VIEW_FACTOR;
    /**
     * Control how much the average rating
     * factor into the trending score
     *
     * @value default {@code 1.0}
     */
    private final Double RATING_FACTOR;
    /**
     * Number of serie to fetch
     *
     * @value default {@code 10}
     */
    private final Integer LIMIT;
    private final RowMapper<SerieTrendingScore> serieTrendingScoreMapper;
    private List<SerieTrendingScore> trendingSeries;
    private Instant lastUpdate;
    TrendingSerieService(
            JdbcTemplate jdbc,
            @Value("${services.trending-serie.view-factor}") final Double viewFactor,
            @Value("${services.trending-serie.rating-factor}") final Double ratingFactor,
            @Value("${services.trending-serie.limit}") final Integer limit,
            @Value("${services.trending-serie.compute-interval}") final String computeInterval
    ) {
        // DI
        this.jdbc = jdbc;
        // config
        VIEW_FACTOR      = viewFactor;
        RATING_FACTOR    = ratingFactor;
        LIMIT            = limit;
        COMPUTE_INTERVAL = DurationCodecUtil.decode(computeInterval);
        // caching
        serieTrendingScoreMapper = new SerieTrendingScoreRowMapper();
        lastUpdate      = Instant.ofEpochSecond(0);
        trendingSeries  = Collections.emptyList();
    }

    /**
     * Fetch & compute the trending serie list
     *
     * @param viewFactor Control the view factor
     * @param ratingFactor Control the rating factor
     * @param limit Nb. max of serie to fetch
     * @return A {@link List} of {@link SerieTrendingScore}
     */
    private List<SerieTrendingScore> computeTrendingSeries(Double viewFactor, Double ratingFactor, Integer limit) {
        return jdbc.query("""
                SELECT
                    *,
                    (history_score * ?) + (rating_score * ?) AS trending_score
                FROM
                    serie_score
                ORDER BY
                    trending_score DESC
                LIMIT ?
                """,
                serieTrendingScoreMapper,
                viewFactor,
                ratingFactor,
                limit
        );
    }

    /**
     * Get the computed list of trending serie
     *
     * @return A {@link List} of {@link SerieTrendingScore}
     */
    public List<SerieTrendingScore> getTrendingSeries() {
        if(lastUpdate.isBefore(Instant.now().minus(COMPUTE_INTERVAL))) {
            trendingSeries = computeTrendingSeries(VIEW_FACTOR, RATING_FACTOR, LIMIT);
            lastUpdate = Instant.now();
        }
        return trendingSeries.stream().toList();
    }
}
