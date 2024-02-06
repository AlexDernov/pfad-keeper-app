package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.repos.RoutesRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoutesControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RoutesRepo routesRepo;

    @DirtiesContext
    @Test
    void getRoutesTest_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        // GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", dateTime3));

        // WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/routes"))

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
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }]
                        """))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

    }

    @DirtiesContext
    @Test
    void getRouteByIdTest_shouldReturnObjectWithTheId() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Route route = routesRepo.save(new Route("test-id", coordsList, "Berlin", dateTime3));
        String id = route.id();
        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/routes/{id}", id))
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
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }
                        """))
                .andReturn();
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

    }

    @DirtiesContext
    @Test
    void getRoutesByNoExistingIdTest_shouldReturnNoObject() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Route route = routesRepo.save(new Route("test-id", coordsList, "Berlin", dateTime3));
        String nonExistingId = "nonExistingId";
        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/routes/{id}", nonExistingId))
                //THEN
                .andExpect(status().isNotFound())
                .andReturn();
        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());

    }

    @DirtiesContext
    @Test
    void addRouteTest_shouldReturnOneObject_whenObjectWasSavedInRepository() throws Exception {
        // GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", dateTime3));
        // WHEN
        mvc.perform(MockMvcRequestBuilders.post("/api/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
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
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }
                                """)
                )
                // THEN
                .andExpect(status().isOk())
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
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }
                        """))


                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @DirtiesContext
    @Test
    void updateRouteTest_shouldReturnRouteWithUpdatedAuthor_whenRouteWithUpdatedAuthorSent() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", dateTime3));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/routes/test-id")
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
                                                  "name": "München",
                                                  "dateTime": "2020-05-04T10:00:00"
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
                                                  "name": "München",
                                                  "dateTime": "2020-05-04T10:00:00"
                                              }
                        """))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DirtiesContext
    @Test
    void deleteRouteById_shouldReturnRoute_whenThisObjectWasDeletedFromRepository() throws Exception {
        //GIVEN
        LocalDateTime dateTime1 = LocalDateTime.of(2014, Month.JANUARY, 1, 8, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.MARCH, 30, 4, 24);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.APRIL, 4,10, 30);

        Coords coords1 = new Coords("9", dateTime1, "284857", "325325");
        Coords coords2 = new Coords("8", dateTime2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", dateTime3));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/routes/test-id"))

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
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }
                               """))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }
}