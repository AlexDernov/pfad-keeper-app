package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.Image;
import de.alexdernov.backend.models.ImagesDto;
import de.alexdernov.backend.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor

public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public List<Image> getImages() {
        return imageService.getImages();
    }

    @GetMapping("/{id}")
    public Image getImageById(@PathVariable String id) {
        return imageService.getById(id);
    }

    @GetMapping("/route/{id}")
    public List<Image> getImageByRouteId(@PathVariable String id) {return imageService.getByRouteId(id);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Image addImage(@RequestPart(name="file") MultipartFile file, @RequestPart(name="data") ImagesDto imageDto) throws IOException {
        return imageService.addImage(imageDto, file);
    }

    @PutMapping("/{id}")
    public Image updateImage(@PathVariable String id, @RequestBody Image image) {
        return imageService.updateImage(image, id);
    }

    @DeleteMapping("/{id}")
    public Image deleteImageById(@PathVariable String id) {
        return imageService.deleteImageById(id);
    }

}
