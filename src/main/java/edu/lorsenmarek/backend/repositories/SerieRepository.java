package edu.lorsenmarek.backend.repositories;
import edu.lorsenmarek.backend.models.Serie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SerieRepository extends CrudRepository<Serie, Long>, SerieRepositoryCustom {

}