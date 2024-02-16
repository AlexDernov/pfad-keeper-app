package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.Images;
import de.alexdernov.backend.models.ImagesDto;
import de.alexdernov.backend.services.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor

public class ImagesController {

    private final ImagesService imagesService;

    @GetMapping
    public List<Images> getImages() {
        return imagesService.getImages();
    }

    @GetMapping("/{id}")
    public Images getImageById(@PathVariable String id) {
        return imagesService.getById(id);
    }

    @GetMapping("/route/{id}")
    public List<Images> getImageByRouteId(@PathVariable String id) {return imagesService.getByRouteId(id);}

    @PostMapping
    public Images addImage(@RequestBody ImagesDto imageDto) {
        return imagesService.addImage(imageDto);
    }

    @PutMapping("/{id}")
    public Images updateImage(@PathVariable String id, @RequestBody Images image) {
        return imagesService.updateImage(image, id);
    }

    @DeleteMapping("/{id}")
    public Images deleteImageById(@PathVariable String id) {
        return imagesService.deleteImageById(id);
    }

}
