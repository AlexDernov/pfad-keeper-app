package de.alexdernov.backend.services;

import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.CoordsDto;
import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.models.RouteDto;
import de.alexdernov.backend.repos.RoutesRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RouteServiceTest {
    RoutesRepo routesRepo = Mockito.mock(RoutesRepo.class);
    IdService idService = Mockito.mock(IdService.class);
    RouteService routeService = new RouteService(routesRepo,idService);


    @Test
    void getRouteTest_returnListOfAllRouts() {
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Mockito.when(routesRepo.findAll()).thenReturn(List.of(
                new Route("1", coordsList, "Berlin", dateTime3),
                new Route("2", coordsList, "Berlin", dateTime2)
        ));
        //WHEN
        List<Route> actual = routeService.getRoute();
        //THEN
        assertEquals(List.of(
                new Route("1", coordsList, "Berlin", dateTime3),
                new Route("2", coordsList, "Berlin", dateTime2)
        ),actual);
        Mockito.verify(routesRepo, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(routesRepo);
    }

    @Test
    void updateBookTest_returnUpdateRoute_whenRouteUpdatsSent() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);

        Route updatedRoute =   new Route("1", coordsList, "Berlin", dateTime3);

        Mockito.when(routesRepo.save(Mockito.any())).thenReturn(updatedRoute);


        //WHEN
        Route actual = routeService.updateRoute(updatedRoute);

        //THEN
        assertEquals(updatedRoute, actual);

        Mockito.verify(routesRepo, Mockito.times(1)).save(updatedRoute);
        Mockito.verifyNoMoreInteractions(routesRepo);
    }

    @Test
    void getById() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        String expectedId = "1";
        Mockito.when(routesRepo.findById(expectedId)).thenReturn(Optional.of(
                new Route("1", coordsList, "Berlin", dateTime3)
        ));
        RouteService routeService = new RouteService(routesRepo,idService);
        //WHEN
        Route foundRoute = routeService.getById(expectedId);
        //THEN
        Assertions.assertNotNull(foundRoute);
        Assertions.assertEquals(expectedId, foundRoute.id());
    }

    @Test
    void deleteRouteById() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Mockito.when(routesRepo.findById(Mockito.any())).thenReturn(
                Optional.of(
                        new Route("1", coordsList, "Berlin", dateTime3)
                ));

        RouteService routeService = new RouteService(routesRepo,idService);

        //WHEN
        Route actual = routeService.deleteRouteById("1");

        //THEN
        assertEquals(
                new Route("1", coordsList, "Berlin", dateTime3)
                , actual);

        Mockito.verify(routesRepo, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(routesRepo, Mockito.times(1)).delete(Mockito.any());
        Mockito.verifyNoMoreInteractions(routesRepo);
    }

    @Test
    void addRoute() {
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        RouteDto routeDto = new RouteDto(coordsList, "Berlin", dateTime3);

        Route route = new Route("test-id", coordsList, "Berlin", dateTime3);


        // GIVEN
        Mockito.when(routesRepo.save(route)).thenReturn(route);
        Mockito.when(idService.newId()).thenReturn("test-id");

        RouteService bookService = new RouteService(routesRepo, idService);


        // WHEN
        Route actual = routeService.addRoute(routeDto);

        // THEN
        Mockito.verify(routesRepo).save(route);
        Mockito.verify(idService).newId();

        Route expected = new Route("test-id", coordsList, "Berlin", dateTime3);
        assertEquals(expected, actual);
    }
}