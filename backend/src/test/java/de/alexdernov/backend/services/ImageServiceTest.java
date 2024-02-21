package de.alexdernov.backend.services;

import de.alexdernov.backend.models.*;
import de.alexdernov.backend.repos.ImageRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ImageServiceTest {
    ImageRepo imageRepo = Mockito.mock(ImageRepo.class);
    IdService idService = Mockito.mock(IdService.class);
    CloudinaryService cloudinaryService =Mockito.mock(CloudinaryService.class);
    ImageService imageService = new ImageService(imageRepo, idService, cloudinaryService);

    @Test
    void getImagesTest_whenGetImages_returnListOfAllImages() {
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        Mockito.when(imageRepo.findAll()).thenReturn(List.of(
                new Image("1", coords1, "url1", "routeId1"),
                new Image("2", coords2, "url2", "routeId2")
        ));
        //WHEN
        List<Image> actual = imageService.getImages();
        //THEN
        assertEquals(List.of(
                new Image("1", coords1, "url1", "routeId1"),
                new Image("2", coords2, "url2", "routeId2")
        ), actual);
        Mockito.verify(imageRepo, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(imageRepo);
    }

    @Test
    void updateImageTest_whenImageUpdatesSent_returnUpdateImage() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Image updatedImage = new Image("1", coords1, "url1", "routeId1");
        Mockito.when(imageRepo.save(any())).thenReturn(updatedImage);
        //WHEN
        Image actual = imageService.updateImage(updatedImage, "1");
        //THEN
        assertEquals(updatedImage, actual);
        Mockito.verify(imageRepo, Mockito.times(1)).save(updatedImage);
        Mockito.verifyNoMoreInteractions(imageRepo);
    }

    @Test
    void getImageByIdTest_whenId_thenReturnImageWithTheId() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        String expectedId = "1";
        Mockito.when(imageRepo.findById(expectedId)).thenReturn(Optional.of(
                new Image("1", coords1, "url1", "routeId1")
        ));
        //WHEN
        Image foundImage = imageService.getById(expectedId);
        //THEN
        Assertions.assertNotNull(foundImage);
        Assertions.assertEquals(expectedId, foundImage.id());
    }

    @Test
    void getImageByIdTest_whenIdNotFound_thenThrowResponseStatusException() {
        // GIVEN
        String expectedId = "nonExistentId";
        Mockito.when(imageRepo.findById(expectedId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResponseStatusException.class, () -> imageService.getById(expectedId));
    }
@Test
void getImageByRouteIdTest_whenRouteId_thenReturnListOdImagesWithTheRouteId() {
    //GIVEN
    LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
    LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
    Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
    Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
    String expectedRouteId = "routeId1";

    Mockito.when(imageRepo.findAllByRouteId(expectedRouteId)).thenReturn(List.of(
            new Image("1", coords1, "url1", "routeId1"),
            new Image("3", coords2, "url3", "routeId1")
    ));
    //WHEN
    List<Image> foundImages = imageService.getByRouteId(expectedRouteId);
    //THEN
    Assertions.assertNotNull(foundImages);
    Assertions.assertEquals(expectedRouteId, foundImages.getFirst().routeId());
}

    @Test
    void getImageByRouteIdTest_whenNoImagesWithRouteId_thenReturnEmptyList() {
        // GIVEN
        String expectedRouteId = "nonExistentRouteId";
        Mockito.when(imageRepo.findAllByRouteId(expectedRouteId)).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> imageService.getById(expectedRouteId));
    }


    @Test
    void deleteImageByIdTest_whenId_thenReturnRouteWithTheId() {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Mockito.when(imageRepo.findById(any())).thenReturn(
                Optional.of(
                        new Image("1", coords1, "url1", "routeId1")
                ));
        //WHEN
       Image actual = imageService.deleteImageById("1");

        //THEN
        assertEquals(
                new Image("1", coords1, "url1", "routeId1")
                , actual);

        Mockito.verify(imageRepo, Mockito.times(1)).findById(any());
        Mockito.verify(imageRepo, Mockito.times(1)).delete(any());
        Mockito.verifyNoMoreInteractions(imageRepo);
    }

    @Test
    void addImageTest_whenNewRouteDto_thenAddTheRouteToRepo() throws Exception{
        // GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        ImagesDto imagesDto = new ImagesDto(coords1,  "routeId1");
        Image image = new Image("test-id", coords1, "url1", "routeId1");
        MockMultipartFile file = new MockMultipartFile(
                "file", "Datei.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());

        Mockito.when(imageRepo.save(image)).thenReturn(image);
        Mockito.when(idService.newId()).thenReturn("test-id");
        Mockito.when(cloudinaryService.uploadFile(any(MultipartFile.class))).thenReturn("url1");
        // WHEN
        Image actual = imageService.addImage(imagesDto, file);

        // THEN
        Mockito.verify(imageRepo).save(image);
        Mockito.verify(idService).newId();
        Mockito.verify(cloudinaryService).uploadFile(any(MultipartFile.class));
        Image expected = new Image("test-id", coords1, "url1", "routeId1");
        assertEquals(expected, actual);
    }
}