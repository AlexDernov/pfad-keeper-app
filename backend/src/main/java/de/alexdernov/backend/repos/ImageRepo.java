package de.alexdernov.backend.repos;

import de.alexdernov.backend.models.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends MongoRepository<Image, String> {

    List<Image> findAllByRouteId(String id);
}
