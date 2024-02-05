package de.alexdernov.backend.repos;

import de.alexdernov.backend.models.Coords;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordsRepo extends MongoRepository<Coords, String> {
}
