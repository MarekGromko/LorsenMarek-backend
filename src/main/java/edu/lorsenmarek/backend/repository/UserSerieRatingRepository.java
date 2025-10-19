package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.converter.jdbc.UserSerieRatingRowMapper;
import edu.lorsenmarek.backend.model.UserSerieRating;
import edu.lorsenmarek.backend.repository.base.CompositeIdsCrudRepository;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link UserSerieRating} table
 *
 * @author Marek Gromko
 */
@Repository
public class UserSerieRatingRepository extends CompositeIdsCrudRepository<UserSerieRating, UserSerieRatingRepository.Ids> {
    /**
     * Composite id to locate a {@link UserSerieRatingRepository}
     * @param userId the user id
     * @param serieId the serie id
     */
    public record Ids(Long userId, Long serieId){}

    /**
     * Create a new {@link UserSerieRatingRepository}
     * @param jdbc depends on {@link JdbcTemplate}
     * @param jdbcMap depends on {@link JdbcMappingContext}
     */
    public UserSerieRatingRepository(
            final JdbcTemplate jdbc,
            final JdbcMappingContext jdbcMap
    ) {
        super(jdbc, jdbcMap, new UserSerieRatingRowMapper());
    }
    @Override
    protected Object extractId(String fieldName, Ids ids) {
        return switch (fieldName) {
            case "userId" -> ids.userId();
            case "serieId" -> ids.serieId();
            default -> null;
        };
    }
}
