package edu.lorsenmarek.backend.repository;
import edu.lorsenmarek.backend.model.Serie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Serie} entities.
 * @author Lorsen Lamour
 * @version 1.0
 */
@Repository
public interface SerieRepository extends CrudRepository<Serie, Long>, SerieRepositoryCustom {
}