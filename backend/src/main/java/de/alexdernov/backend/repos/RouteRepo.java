package de.alexdernov.backend.repos;

import de.alexdernov.backend.models.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepo extends MongoRepository<Route, String> {
}
