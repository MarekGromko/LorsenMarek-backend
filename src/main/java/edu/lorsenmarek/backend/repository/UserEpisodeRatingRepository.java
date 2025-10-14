package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.converter.jdbc.UserEpisodeRatingRowMapper;
import edu.lorsenmarek.backend.model.UserEpisodeRating;
import edu.lorsenmarek.backend.repository.base.CompositeIdsCrudRepository;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *  Repository to access {@link UserEpisodeRating} table
 *
 * @author Marek Gromko
 */
@Repository
public class UserEpisodeRatingRepository extends CompositeIdsCrudRepository<UserEpisodeRating, UserEpisodeRatingRepository.Ids> {
    public record Ids(Long userId, Long episodeId){}
    public UserEpisodeRatingRepository(
            final JdbcTemplate jdbc,
            final JdbcMappingContext jdbcMap
    ) {
        super(jdbc, jdbcMap, new UserEpisodeRatingRowMapper());
    }
    @Override
    protected Object extractId(String fieldName, Ids ids) {
        return switch (fieldName) {
            case "userId" -> ids.userId();
            case "episodeId" -> ids.episodeId();
            default -> null;
        };
    }
}
