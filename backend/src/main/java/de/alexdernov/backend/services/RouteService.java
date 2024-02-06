package de.alexdernov.backend.services;

import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.models.RouteDto;
import de.alexdernov.backend.repos.RoutesRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RouteService {

    private final RoutesRepo routesRepo;
    private final IdService idService;

    public RouteService(RoutesRepo booksRepo, IdService idService) {
        this.routesRepo = booksRepo;
        this.idService = idService;
    }

    public List<Route> getRoute() {
        return routesRepo.findAll();
    }

    public Route updateRoute(Route route) {
        return routesRepo.save(route);
    }

    public Route getById(String id) {
        Optional<Route> routeById = routesRepo.findById(id);
        if (routeById.isPresent()) {
            return new Route(routeById.get().id(), routeById.get().coords(), routeById.get().name(), routeById.get().dateTime());
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No route with such id!"));
    }

    public Route deleteRouteById(String id) {

        Optional<Route> byId = routesRepo.findById(id);
        if (byId.isPresent()) {
            routesRepo.delete(byId.get());
            return byId.get();
        }
        throw (new NoSuchElementException());
    }

    public Route addRoute(RouteDto route) {
        String id = idService.newId();
        Route routeNew = new Route(id, route.coords(), route.name(), route.dateTime());
        return routesRepo.save(routeNew);
    }
}
