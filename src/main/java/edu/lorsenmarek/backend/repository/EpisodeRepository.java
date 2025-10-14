package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.Episode;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access the {@link Episode} table
 */
public interface EpisodeRepository extends CrudRepository<Episode, Long> {
}
