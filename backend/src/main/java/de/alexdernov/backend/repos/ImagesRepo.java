package de.alexdernov.backend.repos;

import de.alexdernov.backend.models.Images;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepo extends MongoRepository<Images, String> {

    List<Images> findAllByRouteId(String id);
}
