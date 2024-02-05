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
        LocalDate date1 = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate date2 = LocalDate.of(2024, Month.MARCH, 30);
        LocalDate date3 = LocalDate.of(2024, Month.APRIL, 4);

        Coords coords1 = new Coords("9", date1, "284857", "325325");
        Coords coords2 = new Coords("8", date2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", date3));

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
                          "date": "2024-03-30",
                          "longitude": "19842798",
                          "latitude": "2343587"
                          },
                           {
                           "id": "9",
                           "date":"2014-01-01",
                           "longitude": "284857",
                           "latitude": "325325"
                           }],
                           "name": "Berlin",
                           "date": "2024-04-04"
                         }]
                        """))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

    }

    @DirtiesContext
    @Test
    void getRouteByIdTest_shouldReturnObjectWithTheId() throws Exception {
        //GIVEN
        LocalDate date1 = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate date2 = LocalDate.of(2024, Month.MARCH, 30);
        LocalDate date3 = LocalDate.of(2024, Month.APRIL, 4);

        Coords coords1 = new Coords("9", date1, "284857", "325325");
        Coords coords2 = new Coords("8", date2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Route route = routesRepo.save(new Route("test-id", coordsList, "Berlin", date3));
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
                          "date": "2024-03-30",
                          "longitude": "19842798",
                          "latitude": "2343587"
                          },
                           {
                           "id": "9",
                           "date":"2014-01-01",
                           "longitude": "284857",
                           "latitude": "325325"
                           }],
                           "name": "Berlin",
                           "date": "2024-04-04"
                         }
                        """))
                .andReturn();
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

    }

    @DirtiesContext
    @Test
    void getRoutesByNoExistingIdTest_shouldReturnNoObject() throws Exception {
        //GIVEN
        LocalDate date1 = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate date2 = LocalDate.of(2024, Month.MARCH, 30);
        LocalDate date3 = LocalDate.of(2024, Month.APRIL, 4);

        Coords coords1 = new Coords("9", date1, "284857", "325325");
        Coords coords2 = new Coords("8", date2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        Route route = routesRepo.save(new Route("test-id", coordsList, "Berlin", date3));
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
        LocalDate date1 = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate date2 = LocalDate.of(2024, Month.MARCH, 30);
        LocalDate date3 = LocalDate.of(2024, Month.APRIL, 4);

        Coords coords1 = new Coords("9", date1, "284857", "325325");
        Coords coords2 = new Coords("8", date2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", date3));
        // WHEN
        mvc.perform(MockMvcRequestBuilders.post("/api/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                   "coords": [
                                  {
                                  "id": "8",
                                  "date": "2024-03-30",
                                  "longitude": "19842798",
                                  "latitude": "2343587"
                                  },
                                   {
                                   "id": "9",
                                   "date":"2014-01-01",
                                   "longitude": "284857",
                                   "latitude": "325325"
                                   }],
                                   "name": "Berlin",
                                   "date": "2024-04-04"
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
                                  "date": "2024-03-30",
                                  "longitude": "19842798",
                                  "latitude": "2343587"
                                  },
                                   {
                                   "id": "9",
                                   "date":"2014-01-01",
                                   "longitude": "284857",
                                   "latitude": "325325"
                                   }],
                                   "name": "Berlin",
                                   "date": "2024-04-04"
                                 }
                        """))


                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @DirtiesContext
    @Test
    void updateRouteTest_shouldReturnRouteWithUpdatedAuthor_whenRouteWithUpdatedAuthorSent() throws Exception {
        //GIVEN
        LocalDate date1 = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate date2 = LocalDate.of(2024, Month.MARCH, 30);
        LocalDate date3 = LocalDate.of(2024, Month.APRIL, 4);

        Coords coords1 = new Coords("9", date1, "284857", "325325");
        Coords coords2 = new Coords("8", date2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", date3));

        //WHEN
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/routes/test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                                                       
                                           {
                           "id": "test-id",
                           "coords": [
                          {
                          "id": "8",
                          "date": "2024-03-30",
                          "longitude": "19842798",
                          "latitude": "2343587"
                          },
                           {
                           "id": "9",
                           "date":"2014-01-01",
                           "longitude": "284986",
                           "latitude": "3248658"
                           }],
                           "name": "München",
                           "date": "2024-04-04"
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
                          "date": "2024-03-30",
                          "longitude": "19842798",
                          "latitude": "2343587"
                          },
                           {
                           "id": "9",
                           "date":"2014-01-01",
                           "longitude": "284986",
                           "latitude": "3248658"
                           }],
                           "name": "München",
                           "date": "2024-04-04"
                         }
                        """))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @DirtiesContext
    @Test
    void deleteRouteById_shouldReturnRoute_whenThisObjectWasDeletedFromRepository() throws Exception {
        //GIVEN
        LocalDate date1 = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate date2 = LocalDate.of(2024, Month.MARCH, 30);
        LocalDate date3 = LocalDate.of(2024, Month.APRIL, 4);

        Coords coords1 = new Coords("9", date1, "284857", "325325");
        Coords coords2 = new Coords("8", date2, "19842798", "2343587");
        List<Coords> coordsList = new ArrayList<>();
        coordsList.add(coords1);
        coordsList.add(coords2);
        routesRepo.save(new Route("test-id", coordsList, "Berlin", date3));

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
                         "date": "2024-03-30",
                         "longitude": "19842798",
                         "latitude": "2343587"
                         },
                          {
                          "id": "9",
                          "date":"2014-01-01",
                          "longitude": "284857",
                          "latitude": "325325"
                          }],
                          "name": "Berlin",
                          "date": "2024-04-04"
                        }
                               """))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }
}