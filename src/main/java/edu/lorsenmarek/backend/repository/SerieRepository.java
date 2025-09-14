import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface SeriesRepository extends JpaRepository<Series, Long> {
    List<Series> findByGenre(String genre);
}