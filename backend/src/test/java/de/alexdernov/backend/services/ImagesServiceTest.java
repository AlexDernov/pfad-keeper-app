package de.alexdernov.backend.services;

import de.alexdernov.backend.models.*;
import de.alexdernov.backend.repos.ImagesRepo;
import de.alexdernov.backend.repos.RouteRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ImagesServiceTest {
    ImagesRepo imagesRepo = Mockito.mock(ImagesRepo.class);
    IdService idService = Mockito.mock(IdService.class);
    ImagesService imagesService = new ImagesService(imagesRepo, idService);

    @Test
    void getImagesTest_whenGetImages_returnListOfAllImages() {
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Mockito.when(imagesRepo.findAll()).thenReturn(List.of(
                new Images("1", coordsList, "url1", "routeId1"),
                new Images("2", coordsList, "url2", "routeId2")
        ));
        //WHEN
        List<Images> actual = imagesService.getImages();
        //THEN
        assertEquals(List.of(
                new Images("1", coordsList, "url1", "routeId1"),
                new Images("2", coordsList, "url2", "routeId2")
        ), actual);
        Mockito.verify(imagesRepo, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(imagesRepo);
    }

    @Test
    void updateImageTest_whenImageUpdatesSent_returnUpdateImage() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);

        Images updatedImage = new Images("1", coordsList, "url1", "routeId1");

        Mockito.when(imagesRepo.save(Mockito.any())).thenReturn(updatedImage);

        //WHEN
        Images actual = imagesService.updateImage(updatedImage, "1");

        //THEN
        assertEquals(updatedImage, actual);

        Mockito.verify(imagesRepo, Mockito.times(1)).save(updatedImage);
        Mockito.verifyNoMoreInteractions(imagesRepo);
    }

    @Test
    void getImageByIdTest_whenId_thenReturnImageWithTheId() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        String expectedId = "1";

        Mockito.when(imagesRepo.findById(expectedId)).thenReturn(Optional.of(
                new Images("1", coordsList, "url1", "routeId1")
        ));

        //WHEN
        Images foundImage = imagesService.getById(expectedId);
        //THEN
        Assertions.assertNotNull(foundImage);
        Assertions.assertEquals(expectedId, foundImage.id());
    }

    @Test
    void getImageByIdTest_whenIdNotFound_thenThrowResponseStatusException() {
        // GIVEN
        String expectedId = "nonExistentId";
        Mockito.when(imagesRepo.findById(expectedId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResponseStatusException.class, () -> imagesService.getById(expectedId));
    }
@Test
void getImageByRouteIdTest_whenRouteId_thenReturnListOdImagesWithTheRouteId() {
    //GIVEN
    LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
    LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

    Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
    Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
    List<Coords> coordsList = new ArrayList<>();
    coordsList.add(coords1);
    coordsList.add(coords2);
    String expectedRouteId = "routeId1";

    Mockito.when(imagesRepo.findAllByRouteId(expectedRouteId)).thenReturn(List.of(
            new Images("1", coordsList, "url1", "routeId1"),
            new Images("3", coordsList, "url3", "routeId1")
    ));

    //WHEN
    List<Images> foundImages = imagesService.getByRouteId(expectedRouteId);
    //THEN
    Assertions.assertNotNull(foundImages);
    Assertions.assertEquals(expectedRouteId, foundImages.getFirst().routeId());
}

    @Test
    void getImageByRouteIdTest_whenNoImagesWithRouteId_thenReturnEmptyList() {
        // GIVEN
        String expectedRouteId = "nonExistentRouteId";
        Mockito.when(imagesRepo.findAllByRouteId(expectedRouteId)).thenReturn(Collections.emptyList());

        // WHEN
        List<Images> foundImages = imagesService.getByRouteId(expectedRouteId);

        // THEN
        Assertions.assertNotNull(foundImages);
        Assertions.assertTrue(foundImages.isEmpty());
    }

    @Test
    void deleteImageByIdTest_whenId_thenReturnRouteWithTheId() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Mockito.when(imagesRepo.findById(Mockito.any())).thenReturn(
                Optional.of(
                        new Images("1", coordsList, "url1", "routeId1")
                ));

        //WHEN
       Images actual = imagesService.deleteImageById("1");

        //THEN
        assertEquals(
                new Images("1", coordsList, "url1", "routeId1")
                , actual);

        Mockito.verify(imagesRepo, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(imagesRepo, Mockito.times(1)).delete(Mockito.any());
        Mockito.verifyNoMoreInteractions(imagesRepo);
    }

    @Test
    void addImageTest_whenNewRouteDto_thenAddTheRouteToRepo() {
        // GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);

        ImagesDto imagesDto = new ImagesDto(coordsList, "url1", "routeId1");
        Images image = new Images("test-id", coordsList, "url1", "routeId1");

        Mockito.when(imagesRepo.save(image)).thenReturn(image);
        Mockito.when(idService.newId()).thenReturn("test-id");

        // WHEN
        Images actual = imagesService.addImage(imagesDto);

        // THEN
        Mockito.verify(imagesRepo).save(image);
        Mockito.verify(idService).newId();

        Images expected = new Images("test-id", coordsList, "url1", "routeId1");
        assertEquals(expected, actual);
    }
}