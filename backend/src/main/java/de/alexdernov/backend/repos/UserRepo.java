package de.alexdernov.backend.repos;

import de.alexdernov.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    Boolean existsByEmail(String email);

    User getUserByEmail(String email);
}
