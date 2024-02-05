package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.CoordsDto;
import de.alexdernov.backend.services.CoordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coords")
@RequiredArgsConstructor
public class CoordsController {
    private final CoordsService coordsService;

    @GetMapping
    public List<Coords> getCoords() {
        return coordsService.getCoords();
    }

    @GetMapping("/{id}")
    public Coords getCoordsById(@PathVariable String id) {
        return coordsService.getById(id);
    }

    @PostMapping
    public Coords addCoords(@RequestBody CoordsDto coordsDto) {
        return coordsService.addCoords(coordsDto);
    }

    @PutMapping("/{id}")
    public Coords updateCoords(@RequestBody Coords coords) {
        return coordsService.updateCoords(coords);
    }

    @DeleteMapping("/{id}")
    public Coords deleteCoordsById(@PathVariable String id) {
        return coordsService.deleteCoordsById(id);
    }

}
