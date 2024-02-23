package de.alexdernov.backend.services;

import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.models.RouteDto;
import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.repos.RouteRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepo routeRepo;
    private final IdService idService;

    public RouteService(RouteRepo routeRepo, IdService idService) {
        this.routeRepo = routeRepo;
        this.idService = idService;
    }

    public List<Route> getRoute() {
        return routeRepo.findAll();
    }

    public Route updateRoute(Route route, String id) {
        return routeRepo.save(route.withId(id));
    }

    public Route getById(String id) {
        Optional<Route> routeById = routeRepo.findById(id);
        if (routeById.isPresent()) {
            return new Route(routeById.get().id(), routeById.get().coords(), routeById.get().members(),routeById.get().name(), routeById.get().dateTime());
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No route with such id!"));
    }
    public Route deleteUserDtoFromMembersList(String routeId, UserDto userToDelete) {
        Optional<Route> optionalRoute = routeRepo.findById(routeId);
        if (optionalRoute.isPresent()) {
            Route route = optionalRoute.get();
            List<UserDto> updatedMembers = route.members().stream()
                    .filter(member -> !member.email().equals(userToDelete.email()))
                    .toList();
            return routeRepo.save(route.withMembers(updatedMembers));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No route with such id!");
    }
    public Route deleteRouteById(String id) {

        Optional<Route> byId = routeRepo.findById(id);
        if (byId.isPresent()) {
            routeRepo.delete(byId.get());
            return byId.get();
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No route with such id!"));
    }

    public Route addRoute(RouteDto route) {
        String id = idService.newId();
        Route routeNew = new Route(id, route.coords(), route.members(), route.name(), route.dateTime());
        return routeRepo.save(routeNew);
    }
}
