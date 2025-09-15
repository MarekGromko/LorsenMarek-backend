package edu.lorsenmarek.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.lorsenmarek.backend.model.Serie;
import java.util.*;
public interface SerieRepository extends JpaRepository<Serie, Integer> {
List<Serie> findByGenre(String genre);
}
