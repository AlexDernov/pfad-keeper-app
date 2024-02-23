package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.AuthProvider;
import de.alexdernov.backend.models.User;
import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;

   /* @Test
    void updateMyName_AuthenticatedUser_ReturnsUserDto() throws Exception {
        userRepo.save(new User("1", "Email", "Name"));
        // Perform the request
        mockMvc.perform(post("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name":"Name2"
                                }""")
                        .with(oidcLogin()
                                .userInfoToken(token ->
                                        token.claim("email", "Email"))))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name2"));
    }*/

    @Test
    void updateMyName_UnauthenticatedUser_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(post("/api/users/me"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCurrentUserTest_whenUserWithoutLogin() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        userRepo.save(new User("1", "Email", "Name"));
        mockMvc.perform(get("/api/users")
                        .with(oidcLogin()
                                .userInfoToken(token ->
                                        token.claim("email", "Email"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id": "1",
                        "email":"Email",
                        "name":"Name"
                        }]
                        """));

    }

    @Test
    void getCurrentUserTest_whenUserLogin() throws Exception {
        userRepo.save(new User("1", "Email", "Name"));
        mockMvc.perform(get("/api/users/me")
                        .with(oidcLogin()
                                .userInfoToken(token ->
                                        token.claim("email", "Email"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                        "email":"Email",
                        "name":"Name"
                        }
                        """));
    }

}