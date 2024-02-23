package de.alexdernov.backend.repos;

import de.alexdernov.backend.models.User;
import de.alexdernov.backend.models.UserDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends MongoRepository<User, String> {
    Boolean existsByEmail(String email);
    Optional<User> getUserByEmail(String email);


}
