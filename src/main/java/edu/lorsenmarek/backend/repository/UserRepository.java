package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * @author Lorsen Lamour
 *  @author Marek Gromko
 *
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Find a user by their email
     * @param email the email address to search
     * @return an {@link Optional} containing the {@link  User} if found.
     */
    Optional<User> findByEmail(String email);
}
