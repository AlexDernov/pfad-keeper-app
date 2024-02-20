package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.AuthProvider;
import de.alexdernov.backend.models.User;
import de.alexdernov.backend.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;

    @Test
    void getCurrentUserTest_whenUserWithoutLogin() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
    @Test
    void getCurrentUserTest_whenUserLogin() throws Exception {
        userRepo.save(new User("1", "Email", new ArrayList<>(List.of("1", "2")), "Name", AuthProvider.GOOGLE));
        mockMvc.perform(get("/api/users/me")
                        .with(oidcLogin()
                                .userInfoToken(token ->
                                        token.claim("email", "Email"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Email"));
    }

}