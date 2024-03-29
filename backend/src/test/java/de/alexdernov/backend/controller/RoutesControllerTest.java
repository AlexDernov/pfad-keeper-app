package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.Route;
import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.repos.RouteRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoutesControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RouteRepo routeRepo;

    @Test
    void updateRoutesMembers_Success() throws Exception {
        // GIVEN

        UserDto userToDelete = new UserDto("emailToDelete", "NameToDelete");
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

        routeRepo.save(new Route("test-id", coordsList, new ArrayList<>(List.of(userDto1, userDto2, userToDelete)), "Berlin", dateTime3));

        // WHEN
        mvc.perform(put("/api/routes/membersList/test-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                               {
                             "email":"emailToDelete",
                             "name":"NameToDelete"
                                }
                                                  
                                     """)
                .with(oidcLogin()
                        .userInfoToken(token ->
                                token.claim("email", "Email")))
                )
        // THEN

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
                                                  "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
                                                  }
                                                  ],
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }
                        """))
                .andReturn();
    }
    @DirtiesContext
    @Test
    void getRoutesTest_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
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
        routeRepo.save(new Route("test-id", coordsList, userIds,"Berlin", dateTime3));

        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/routes"))

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
                                                  "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
                                                  }
                                                  ],
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }]
                        """))
                .andReturn();
    }

    @DirtiesContext
    @Test
    void getRouteByIdTest_shouldReturnObjectWithTheId() throws Exception {
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
        Route route = routeRepo.save(new Route("test-id", coordsList, userIds,"Berlin", dateTime3));
        String id = route.id();
        //WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/routes/{id}", id))
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
                                                  "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
                                                  }
                                                  ],
                                                  "name": "Berlin",
                                                  "dateTime": "2024-04-04T10:30:00"
                                              }
                        """))
                .andReturn();
    }

    @DirtiesContext
    @Test
    void getRoutesByNoExistingIdTest_shouldReturnNoObject() throws Exception {
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
        routeRepo.save(new Route("test-id", coordsList, userIds,"Berlin", dateTime3));
        String nonExistingId = "nonExistingId";
        //WHEN
        mvc.perform(MockMvcRequestBuilders.get("/api/routes/{id}", nonExistingId))
                //THEN
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @DirtiesContext
    @Test
    void addRouteTest_shouldReturnOneObject_whenObjectWasSavedInRepository() throws Exception {
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
        routeRepo.save(new Route("test-id", coordsList, userIds,"Berlin", dateTime3));
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
                                                       "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
                                                  }
                                                  ],
                                                       "name": "Berlin",
                                                       "dateTime": "2024-04-04T10:30:00"
                                                   }
                                     """)
                        .with(oidcLogin()
                                .userInfoToken(token ->
                                        token.claim("email", "Email")))
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
                                                  "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
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
    void updateRouteTest_shouldReturnRouteWithUpdatedName_whenRouteWithUpdatedNameSent() throws Exception {
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
        routeRepo.save(new Route("test-id", coordsList, userIds,"Berlin", dateTime3));

        //WHEN
        mvc.perform(put("/api/routes/test-id")
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
                                                       "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
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
                                                   "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
                                                  }
                                                  ],
                                                   "name": "München",
                                                   "dateTime": "2020-05-04T10:00:00"
                                               }
                         """))
                .andReturn();
    }

    @DirtiesContext
    @Test
    void deleteRouteById_shouldReturnRoute_whenThisObjectWasDeletedFromRepository() throws Exception {
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
        routeRepo.save(new Route("test-id", coordsList,userIds, "Berlin", dateTime3));

        //WHEN
        mvc.perform(MockMvcRequestBuilders.delete("/api/routes/test-id"))

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
                                               "members": [
                                                  {
                                                  "email":"Email",
                                                  "name":"Name"
                                                  },
                                                  {
                                                  "email":"Email2",
                                                  "name":"Name2"
                                                  }
                                                  ],
                                               "name": "Berlin",
                                               "dateTime": "2024-04-04T10:30:00"
                                           }
                            """))
                .andReturn();
    }
    @DirtiesContext
    @Test
    void deleteRoutesByNoExistingIdTest_shouldReturnNoObject() throws Exception {
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
        routeRepo.save(new Route("test-id", coordsList, userIds,"Berlin", dateTime3));
        String nonExistingId = "nonExistingId";
        //WHEN
        mvc.perform(MockMvcRequestBuilders.delete("/api/routes/{id}", nonExistingId))

                //THEN
                .andExpect(status().isNotFound())
                .andReturn();
    }
}