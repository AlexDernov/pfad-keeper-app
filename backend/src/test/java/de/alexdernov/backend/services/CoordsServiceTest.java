package de.alexdernov.backend.services;

import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.CoordsDto;
import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.models.RouteDto;
import de.alexdernov.backend.repos.CoordsRepo;
import de.alexdernov.backend.repos.RouteRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CoordsServiceTest {
    CoordsRepo coordsRepo = Mockito.mock(CoordsRepo.class);
    IdService idService = Mockito.mock(IdService.class);
    CoordsService coordsService = new CoordsService(coordsRepo, idService);

    @Test
    void getCoordsTest_returnListOfAllCoords() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Mockito.when(coordsRepo.findAll()).thenReturn(List.of(
                new Coords("1", dateTime1, "256187", "456486"),
                new Coords("2", dateTime2, "965897", "57483")
        ));
        //WHEN
        List<Coords> actual = coordsService.getCoords();
        //THEN
        assertEquals(List.of(new Coords("1", dateTime1, "256187", "456486"),
                new Coords("2", dateTime2, "965897", "57483")), actual);
        Mockito.verify(coordsRepo, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(coordsRepo);


    }

    @Test
    void updateCoordsTest_returnUpdateCoords_whenNewCoords() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        Coords updatedCoords =   new Coords("1", dateTime1, "2345", "6456");

        Mockito.when(coordsRepo.save(Mockito.any())).thenReturn(updatedCoords);

        //WHEN
        Coords actual = coordsService.updateCoords(updatedCoords);

        //THEN
        assertEquals(updatedCoords, actual);

        Mockito.verify(coordsRepo, Mockito.times(1)).save(updatedCoords);
        Mockito.verifyNoMoreInteractions(coordsRepo);
    }

    @Test
    void getById() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        String expectedId = "1";
        Mockito.when(coordsRepo.findById(expectedId)).thenReturn(Optional.of(
                new Coords("1", dateTime1, "2345", "6456")
        ));

        //WHEN
        Coords foundCoords = coordsService.getById(expectedId);
        //THEN
        Assertions.assertNotNull(foundCoords);
        Assertions.assertEquals(expectedId, foundCoords.id());
    }

    @Test
    void deleteCoordsById() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);

        Mockito.when(coordsRepo.findById(Mockito.any())).thenReturn(
                Optional.of(
                        new Coords("1", dateTime1, "2345", "6456")
                ));

        //WHEN
        Coords actual = coordsService.deleteCoordsById("1");

        //THEN
        assertEquals(
                new Coords("1", dateTime1, "2345", "6456")
                , actual);

        Mockito.verify(coordsRepo, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(coordsRepo, Mockito.times(1)).delete(Mockito.any());
        Mockito.verifyNoMoreInteractions(coordsRepo);
    }

    @Test
    void addCoords() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        CoordsDto coordsDto = new CoordsDto( dateTime1, "2345", "6456");
        Coords coords = new Coords("test-id", dateTime1, "2345", "6456");

        Mockito.when(coordsRepo.save(coords)).thenReturn(coords);
        Mockito.when(idService.newId()).thenReturn("test-id");

        // WHEN
        Coords actual = coordsService.addCoords(coordsDto);

        // THEN
        Mockito.verify(coordsRepo).save(coords);
        Mockito.verify(idService).newId();

        Coords expected = new Coords("test-id", dateTime1, "2345", "6456");
        assertEquals(expected, actual);
    }
}