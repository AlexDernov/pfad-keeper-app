package de.alexdernov.backend.services;

import de.alexdernov.backend.models.Images;
import de.alexdernov.backend.models.ImagesDto;
import de.alexdernov.backend.repos.ImagesRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImagesService {
    private final ImagesRepo imagesRepo;
    private final IdService idService;
    private final CloudinaryService coloudinaryService;

    public ImagesService(ImagesRepo imagesRepo, IdService idService, CloudinaryService coloudinaryService) {
        this.imagesRepo = imagesRepo;
        this.idService = idService;
        this.coloudinaryService = coloudinaryService;
    }

    public List<Images> getImages() {
        return imagesRepo.findAll();
    }

    public List<Images> getByRouteId(String id) {
        List<Images> imagesByRoutId = imagesRepo.findAllByRouteId(id);
        if (imagesByRoutId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No image with such route id!");
        }
        return imagesByRoutId;
    }

    public Images getById(String id) {
        Optional<Images> imageById = imagesRepo.findById(id);
        if (imageById.isPresent()) {
            return new Images(imageById.get().id(), imageById.get().coords(), imageById.get().url(), imageById.get().routeId());
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No image with such id!"));
    }

    public Images updateImage(Images images, String id) {
        return imagesRepo.save(images.withId(id));
    }

    public Images deleteImageById(String id) {

        Optional<Images> imagebyId = imagesRepo.findById(id);
        if (imagebyId.isPresent()) {
            imagesRepo.delete(imagebyId.get());
            return imagebyId.get();
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No image with such id!"));
    }

    public Images addImage(ImagesDto image, MultipartFile file) throws IOException {
        String url = coloudinaryService.uploadFile(file);
        String id = idService.newId();
        Images imageNew = new Images(id, image.coords(), url, image.routeId());
        return imagesRepo.save(imageNew);
    }
}
