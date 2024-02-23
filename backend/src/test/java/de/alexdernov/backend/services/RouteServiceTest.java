package de.alexdernov.backend.services;

import de.alexdernov.backend.models.*;
import de.alexdernov.backend.repos.RouteRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class RouteServiceTest {
    RouteRepo routeRepo = Mockito.mock(RouteRepo.class);
    IdService idService = Mockito.mock(IdService.class);
    RouteService routeService = new RouteService(routeRepo, idService);


    @Test
    void getRouteTest_whenGetRoute_thenReturnListOfAllRoutes() {
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4, 10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);

        UserDto userDto1 = new UserDto("Email", "Name");
        UserDto userDto2 = new UserDto("Email2", "Name2");
        List<UserDto> userIds = new ArrayList<>();
        userIds.add(userDto1);
        userIds.add(userDto2);
        Mockito.when(routeRepo.findAll()).thenReturn(List.of(
                new Route("1", coordsList, userIds, "Berlin", dateTime3),
                new Route("2", coordsList, userIds, "Berlin", dateTime2)
        ));
        //WHEN
        List<Route> actual = routeService.getRoute();
        //THEN
        assertEquals(List.of(
                new Route("1", coordsList, userIds, "Berlin", dateTime3),
                new Route("2", coordsList, userIds, "Berlin", dateTime2)
        ), actual);
        Mockito.verify(routeRepo, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(routeRepo);
    }

    @Test
    void updateRouteTest_whenRouteUpdatesSent_thenReturnUpdateRoute() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4, 10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);

        UserDto userDto1 = new UserDto("Email", "Name");
        UserDto userDto2 = new UserDto("Email2", "Name2");
        List<UserDto> userIds = new ArrayList<>();
        userIds.add(userDto1);
        userIds.add(userDto2);
        Route updatedRoute = new Route("1", coordsList, userIds, "Berlin", dateTime3);

        Mockito.when(routeRepo.save(any())).thenReturn(updatedRoute);


        //WHEN
        Route actual = routeService.updateRoute(updatedRoute, "1");

        //THEN
        assertEquals(updatedRoute, actual);

        Mockito.verify(routeRepo, Mockito.times(1)).save(updatedRoute);
        Mockito.verifyNoMoreInteractions(routeRepo);
    }

    @Test
    void getRouteByIdTest_whenId_thenReturnRouteWithTheId() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4, 10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        UserDto userDto1 = new UserDto("Email", "Name");
        UserDto userDto2 = new UserDto("Email2", "Name2");
        List<UserDto> userIds = new ArrayList<>();
        userIds.add(userDto1);
        userIds.add(userDto2);
        String expectedId = "1";
        Mockito.when(routeRepo.findById(expectedId)).thenReturn(Optional.of(
                new Route("1", coordsList, userIds, "Berlin", dateTime3)
        ));

        //WHEN
        Route foundRoute = routeService.getById(expectedId);
        //THEN
        Assertions.assertNotNull(foundRoute);
        Assertions.assertEquals(expectedId, foundRoute.id());
    }

    @Test
    void deleteUserDtoFromMembersList_SuccessfullyRemovedUser() {
        // GIVEN
        String routeId = "routeId";
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4, 10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        UserDto userDto1 = new UserDto("Email", "Name");
        UserDto userDto2 = new UserDto("Email2", "Name2");
        UserDto userToDelete = new UserDto("emailToDelete", "NameToDelete");

        Route existingRoute = new Route(routeId, coordsList, new ArrayList<>(List.of(userDto1, userDto2, userToDelete)), "Berlin", dateTime3);
        Mockito.when(routeRepo.findById(routeId)).thenReturn(Optional.of(existingRoute));
        Mockito.when(routeRepo.save(any(Route.class))).thenReturn(existingRoute.withMembers(List.of(userDto1, userDto2)));

        // WHEN
        Route updatedRoute = routeService.deleteUserDtoFromMembersList(routeId, userToDelete);

        // THEN
        Mockito.verify(routeRepo, Mockito.times(1)).findById(routeId);
        Mockito.verify(routeRepo, Mockito.times(1)).save(any(Route.class));

        assertEquals(2, updatedRoute.members().size());
        assertFalse(updatedRoute.members().contains(userToDelete));
    }

    @Test
    void deleteUserDtoFromMembersList_RouteNotFound_ThrowException() {
        // GIVEN
        String routeId = "routeId";
        UserDto userToDelete = new UserDto("emailToDelete", "NameToDelete");

        Mockito.when(routeRepo.findById(routeId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResponseStatusException.class, () -> {
            routeService.deleteUserDtoFromMembersList(routeId, userToDelete);
        });

        Mockito.verify(routeRepo, Mockito.times(1)).findById(routeId);
        Mockito.verify(routeRepo, Mockito.never()).save(any(Route.class));
    }

    @Test
    void deleteRouteByIdTest_whenId_thenReturnRouteWithTheId() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4, 10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        UserDto userDto1 = new UserDto("Email", "Name");
        UserDto userDto2 = new UserDto("Email2", "Name2");
        List<UserDto> userIds = new ArrayList<>();
        userIds.add(userDto1);
        userIds.add(userDto2);
        Mockito.when(routeRepo.findById(any())).thenReturn(
                Optional.of(
                        new Route("1", coordsList, userIds, "Berlin", dateTime3)
                ));

        //WHEN
        Route actual = routeService.deleteRouteById("1");

        //THEN
        assertEquals(
                new Route("1", coordsList, userIds, "Berlin", dateTime3)
                , actual);

        Mockito.verify(routeRepo, Mockito.times(1)).findById(any());
        Mockito.verify(routeRepo, Mockito.times(1)).delete(any());
        Mockito.verifyNoMoreInteractions(routeRepo);
    }

    @Test
    void addRouteTest_whenNewRouteDto_thenAddTheRouteToRepo() {
        // GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4, 10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);

        UserDto userDto1 = new UserDto("Email", "Name");
        UserDto userDto2 = new UserDto("Email2", "Name2");
        List<UserDto> userIds = new ArrayList<>();
        userIds.add(userDto1);
        userIds.add(userDto2);
        RouteDto routeDto = new RouteDto(coordsList, userIds, "Berlin", dateTime3);
        Route route = new Route("test-id", coordsList, userIds, "Berlin", dateTime3);

        Mockito.when(routeRepo.save(route)).thenReturn(route);
        Mockito.when(idService.newId()).thenReturn("test-id");

        // WHEN
        Route actual = routeService.addRoute(routeDto);

        // THEN
        Mockito.verify(routeRepo).save(route);
        Mockito.verify(idService).newId();

        Route expected = new Route("test-id", coordsList, userIds, "Berlin", dateTime3);
        assertEquals(expected, actual);
    }
}