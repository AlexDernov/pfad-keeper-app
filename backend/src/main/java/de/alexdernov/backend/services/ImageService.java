package de.alexdernov.backend.services;

import de.alexdernov.backend.models.Image;
import de.alexdernov.backend.models.ImagesDto;
import de.alexdernov.backend.repos.ImageRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepo imageRepo;
    private final IdService idService;
    private final CloudinaryService cloudinaryService;

    public ImageService(ImageRepo imageRepo, IdService idService, CloudinaryService cloudinaryService) {
        this.imageRepo = imageRepo;
        this.idService = idService;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Image> getImages() {
        return imageRepo.findAll();
    }

    public List<Image> getByRouteId(String id) {
        List<Image> imagesByRoutId = imageRepo.findAllByRouteId(id);
        if (imagesByRoutId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No image with such route id!");
        }
        return imagesByRoutId;
    }

    public Image getById(String id) {
        Optional<Image> imageById = imageRepo.findById(id);
        if (imageById.isPresent()) {
            return new Image(imageById.get().id(), imageById.get().coords(), imageById.get().url(), imageById.get().routeId());
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No image with such id!"));
    }

    public Image updateImage(Image image, String id) {
        return imageRepo.save(image.withId(id));
    }

    public Image deleteImageById(String id) {

        Optional<Image> imageById = imageRepo.findById(id);
        if (imageById.isPresent()) {
            imageRepo.delete(imageById.get());
            return imageById.get();
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No image with such id!"));
    }

    public Image addImage(ImagesDto image, MultipartFile file) throws IOException {
        String url = cloudinaryService.uploadFile(file);
        String id = idService.newId();
        Image imageNew = new Image(id, image.coords(), url, image.routeId());
        return imageRepo.save(imageNew);
    }
}
