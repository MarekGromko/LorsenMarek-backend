package edu.lorsenmarek.backend.repository;

import edu.lorsenmarek.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
User findByEmail(String email);
boolean exitByemail(String email);
}
