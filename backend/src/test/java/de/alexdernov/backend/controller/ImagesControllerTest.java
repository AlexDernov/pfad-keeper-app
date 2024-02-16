package de.alexdernov.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.Images;
import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.repos.ImagesRepo;
import de.alexdernov.backend.repos.RouteRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ImagesControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ImagesRepo imagesRepo;
    @MockBean
    private Cloudinary cloudinary;
    private Uploader uploader = mock(Uploader.class);

    @DirtiesContext
    @Test
    void getImagesTest_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        // GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4, 10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);

        imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));

        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images"))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                           "id": "test-id",
                           "coords": [
                                                      {
                                                          "id": "8",
                                                          "dateTime": "2024-03-30T04:24:00",
                                                          "longitude": "19842798",
                                                          "latitude": "2343587"
                                                      },
                                                      {
                                                          "id": "9",
                                                          "dateTime": "2014-01-01T08:30:00",
                                                          "longitude": "284857",
                                                          "latitude": "325325"
                                                      }
                                                  ],
                                                  "url": "url1",
                                                  "routeId": "routeId1"
                                              }]
                        """))
                .andReturn();
    }
    @DirtiesContext
    @Test
    void getImageByIdTest_shouldReturnObjectWithTheId() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Images image = imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));
        String id = image.id();
        //WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images/{id}", id))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                           "id": "test-id",
                           "coords": [
                                                      {
                                                          "id": "8",
                                                          "dateTime": "2024-03-30T04:24:00",
                                                          "longitude": "19842798",
                                                          "latitude": "2343587"
                                                      },
                                                      {
                                                          "id": "9",
                                                          "dateTime": "2014-01-01T08:30:00",
                                                          "longitude": "284857",
                                                          "latitude": "325325"
                                                      }
                                                  ],
                                                  "url": "url1",
                                                  "routeId": "routeId1"
                                              }
                        """))
                .andReturn();
    }
    @DirtiesContext
    @Test
    void getImageByNoExistingIdTest_whenIdNotFound_thenStatusNotFound() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));
        String nonExistingId = "nonExistingId";
        //WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images/{id}", nonExistingId))
                //THEN
                .andExpect(status().isNotFound())
                .andReturn();
    }
    @DirtiesContext
    @Test
    void getImagesByRouteIdTest_shouldReturnListOfImagesWithTheRouteId() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Images image = imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));
        String routeId = image.routeId();
        //WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images/route/{id}", routeId))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                           "id": "test-id",
                           "coords": [
                                                      {
                                                          "id": "8",
                                                          "dateTime": "2024-03-30T04:24:00",
                                                          "longitude": "19842798",
                                                          "latitude": "2343587"
                                                      },
                                                      {
                                                          "id": "9",
                                                          "dateTime": "2014-01-01T08:30:00",
                                                          "longitude": "284857",
                                                          "latitude": "325325"
                                                      }
                                                  ],
                                                  "url": "url1",
                                                  "routeId": "routeId1"
                                              }
                                              ]
                        """))
                .andReturn();
    }
    @Test
    void getImageByRouteIdTest_whenRouteIdNotFound_thenStatusNotFound() throws Exception {
        // GIVEN
        String nonExistentRouteId = "nonExistentRouteId";

        // WHEN & THEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images/route/{id}", nonExistentRouteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext
    @Test
    void addImageTest_shouldReturnOneObject_whenObjectWasSavedInRepository() throws Exception {
        // GIVEN
        Map map =new HashMap();
        map.put("secure_url", "url1");
        Mockito.when(uploader.upload(any(),any())).thenReturn(map);
        Mockito.when(cloudinary.uploader()).thenReturn(uploader);
        MockMultipartFile file = new MockMultipartFile(
                "file", "Datei.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());
        MockMultipartFile file2 = new MockMultipartFile(
                "data", "", MediaType.APPLICATION_JSON_VALUE, """
                                      {
                                "coords": [
                                                           {
                                                               "id": "8",
                                                               "dateTime": "2024-03-30T04:24:00",
                                                               "longitude": "19842798",
                                                               "latitude": "2343587"
                                                           },
                                                           {
                                                               "id": "9",
                                                               "dateTime": "2014-01-01T08:30:00",
                                                               "longitude": "284857",
                                                               "latitude": "325325"
                                                           }
                                                       ],
                                                       
                                                       "routeId": "routeId1"
                                                   }
                                     """.getBytes());

        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));
        // WHEN
        mvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                        .file(file2)
                        .file(file)
                )
                // THEN
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json("""

                          {
                           "coords": [
                                                      {
                                                          "id": "8",
                                                          "dateTime": "2024-03-30T04:24:00",
                                                          "longitude": "19842798",
                                                          "latitude": "2343587"
                                                      },
                                                      {
                                                          "id": "9",
                                                          "dateTime": "2014-01-01T08:30:00",
                                                          "longitude": "284857",
                                                          "latitude": "325325"
                                                      }
                                                  ],
                                                  "url": "url1",
                                                  "routeId": "routeId1"
                                              }
                        """))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }
    @DirtiesContext
    @Test
    void updateImageTest_shouldReturnImageWithUpdatedRouteId_whenImageWithUpdatedRouteIdSent() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));
        //WHEN
        mvc.perform(MockMvcRequestBuilders.put("/api/images/test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                                                            
                                              {
                                "id": "test-id",
                                "coords": [
                                                           {
                                                               "id": "8",
                                                               "dateTime": "2024-03-30T04:24:00",
                                                               "longitude": "19842798",
                                                               "latitude": "2343587"
                                                           },
                                                           {
                                                               "id": "9",
                                                               "dateTime": "2014-01-01T08:30:00",
                                                               "longitude": "284857",
                                                               "latitude": "325325"
                                                           }
                                                       ],
                                                       "url": "url1",
                                                       "routeId": "routeId3"
                                                   }
                                                """))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "test-id",
                            "coords": [
                                                       {
                                                           "id": "8",
                                                           "dateTime": "2024-03-30T04:24:00",
                                                           "longitude": "19842798",
                                                           "latitude": "2343587"
                                                       },
                                                       {
                                                           "id": "9",
                                                           "dateTime": "2014-01-01T08:30:00",
                                                           "longitude": "284857",
                                                           "latitude": "325325"
                                                       }
                                                   ],
                                                   "url": "url1",
                                                   "routeId": "routeId3"
                                               }
                         """))
                .andReturn();
    }
    @DirtiesContext
    @Test
    void deleteImageById_shouldReturnImage_whenThisObjectWasDeletedFromRepository() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));

        //WHEN
        mvc.perform(MockMvcRequestBuilders.delete("/api/images/test-id"))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                        "id": "test-id",
                        "coords": [
                                                   {
                                                       "id": "8",
                                                       "dateTime": "2024-03-30T04:24:00",
                                                       "longitude": "19842798",
                                                       "latitude": "2343587"
                                                   },
                                                   {
                                                       "id": "9",
                                                       "dateTime": "2014-01-01T08:30:00",
                                                       "longitude": "284857",
                                                       "latitude": "325325"
                                                   }
                                               ],
                                               "url": "url1",
                                               "routeId": "routeId1"
                                           }
                            """))
                .andReturn();
    }
    @DirtiesContext
    @Test
    void deleteRoutesByNoExistingIdTest_shouldReturnNoObject() throws Exception {
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        imagesRepo.save(new Images("test-id", coordsList, "url1", "routeId1"));
        String nonExistingId = "nonExistingId";
        //WHEN
        mvc.perform(MockMvcRequestBuilders.delete("/api/images/{id}", nonExistingId))
                //THEN
                .andExpect(status().isNotFound())
                .andReturn();
    }
}