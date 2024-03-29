package de.alexdernov.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.Image;
import de.alexdernov.backend.repos.ImageRepo;
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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ImageRepo imageRepo;
    @MockBean
    private Cloudinary cloudinary;
    private Uploader uploader = mock(Uploader.class);

    @DirtiesContext
    @Test
    void getImagesTest_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        // GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));

        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images"))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                           "id": "test-id",
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
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
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Image image = imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));
        String id = image.id();
        //WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images/{id}", id))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                           "id": "test-id",
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
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
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));
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
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Image image = imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));
        String routeId = image.routeId();
        //WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/images/route/{id}", routeId))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                           "id": "test-id",
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
                           "url": "url1",
                           "routeId": "routeId1"
                           }]
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
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
                           "routeId": "routeId1"
                           }
                          """.getBytes());

        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));
        // WHEN
        mvc.perform(MockMvcRequestBuilders.multipart("/api/images")
                        .file(file2)
                        .file(file)
                        .with(oidcLogin()
                                .userInfoToken(token ->
                                        token.claim("email", "Email")))
                )
                // THEN
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json("""

                         {
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
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
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));
        //WHEN
        mvc.perform(MockMvcRequestBuilders.put("/api/images/test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                                                            
                           {
                           "id": "test-id",
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
                           "url": "url1",
                           "routeId": "routeId3"
                           }
                           """))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "id": "test-id",
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
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
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));

        //WHEN
        mvc.perform(MockMvcRequestBuilders.delete("/api/images/test-id"))

                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                           "id": "test-id",
                           "coords": {
                                  "id": "9",
                                  "dateTime": "2014-01-01T08:30:00",
                                  "longitude": "284857",
                                  "latitude": "325325"
                           },
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
        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        imageRepo.save(new Image("test-id", coords1, "url1", "routeId1"));
        String nonExistingId = "nonExistingId";
        //WHEN
        mvc.perform(MockMvcRequestBuilders.delete("/api/images/{id}", nonExistingId))
                //THEN
                .andExpect(status().isNotFound())
                .andReturn();
    }
}