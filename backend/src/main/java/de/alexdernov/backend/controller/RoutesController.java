package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.models.RouteDto;
import de.alexdernov.backend.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RoutesController {

    private final RouteService routeService;

    @GetMapping
    public List<Route> getRoutes() {
        return routeService.getRoute();
    }

    @GetMapping("/{id}")
    public Route getRouteById(@PathVariable String id) {
        return routeService.getById(id);
    }

    @PostMapping
    public Route addRoute(@RequestBody RouteDto routeDto) {
        return routeService.addRoute(routeDto);
    }

    @PutMapping("/{id}")
    public Route updateRoute(@PathVariable String id, @RequestBody Route route) {
        return routeService.updateRoute(route, id);
    }

    @DeleteMapping("/{id}")
    public Route deleteRouteById(@PathVariable String id) {
        return routeService.deleteRouteById(id);
    }
}
