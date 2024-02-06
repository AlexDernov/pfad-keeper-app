package de.alexdernov.backend.services;

import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.CoordsDto;
import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.models.RouteDto;
import de.alexdernov.backend.repos.CoordsRepo;
import de.alexdernov.backend.repos.RoutesRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CoordsService {

    private final CoordsRepo coordsRepo;
    private final IdService idService;

    public CoordsService(CoordsRepo coordsRepo, IdService idService) {
        this.coordsRepo = coordsRepo;
        this.idService = idService;
    }

    public List<Coords> getCoords() {
        return coordsRepo.findAll();
    }

    public Coords updateCoords(Coords coords) {
        return coordsRepo.save(coords);
    }

    public Coords getById(String id) {
        Optional<Coords> coordsById = coordsRepo.findById(id);
        if (coordsById.isPresent()) {
            return new Coords(coordsById.get().id(), coordsById.get().dateTime(), coordsById.get().longitude(), coordsById.get().latitude());
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No coords with such id!"));
    }

    public Coords deleteCoordsById(String id) {

        Optional<Coords> byId = coordsRepo.findById(id);
        if (byId.isPresent()) {
            coordsRepo.delete(byId.get());
            return byId.get();
        }
        throw (new NoSuchElementException("No coords with such id!"));
    }

    public Coords addCoords(CoordsDto coords) {
        String id = idService.newId();
        return coordsRepo.save(new Coords(id, coords.dateTime(), coords.longitude(), coords.latitude()));
    }
}
