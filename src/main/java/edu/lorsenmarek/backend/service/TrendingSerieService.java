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

@Service
@Scope("singleton")
public class TrendingSerieService {
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
    private final Duration COMPUTE_INTERVAL;
    private final Double VIEW_FACTOR;
    private final Double RATING_FACTOR;
    private final Integer LIMIT;
    private final RowMapper<SerieTrendingScore> serieTrendingScoreMapper;
    private List<SerieTrendingScore> trendingSeries;
    private Instant lastUpdate;
    TrendingSerieService(
            JdbcTemplate jdbc,
            @Value("${services.trending-serie.view-factor:1}") final Double viewFactor,
            @Value("${services.trending-serie.rating-factor:1.0}") final Double ratingFactor,
            @Value("${services.trending-serie.limit:10}") final Integer limit,
            @Value("${services.trending-serie.compute-interval:PT10M}") final String computeInterval
    ) {
        this.jdbc = jdbc;
        VIEW_FACTOR = viewFactor;
        RATING_FACTOR = ratingFactor;
        LIMIT = limit;
        COMPUTE_INTERVAL = DurationCodecUtil.decode(computeInterval);
        serieTrendingScoreMapper = new SerieTrendingScoreRowMapper();
        lastUpdate      = Instant.ofEpochSecond(0);
        trendingSeries  = Collections.emptyList();
    }
    private List<SerieTrendingScore> computeTrendingSeries(Double viewFactor, Double ratingFactor, Integer limit) {
        return jdbc.query("""
                SELECT
                    serie.*
                    (COUNT(history.user_id) * ?) + (SUM(rating.rating) * ?) AS trending_score
                FROM
                    serie
                    INNER JOIN user_serie_history AS history ON serie.id = history.serie_id
                    INNER JOIN user_serie_rating AS rating ON serie.id = rating.serie_id
                GROUP BY
                    serie.id
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
    public List<SerieTrendingScore> getTrendingSeries() {
        if(lastUpdate.isBefore(Instant.now().minus(COMPUTE_INTERVAL))) {
            trendingSeries = computeTrendingSeries(VIEW_FACTOR, RATING_FACTOR, LIMIT);
            lastUpdate = Instant.now();
        }
        return trendingSeries;
    }
}
