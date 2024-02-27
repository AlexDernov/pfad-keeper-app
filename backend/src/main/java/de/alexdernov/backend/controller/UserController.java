package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.User;
import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserDto getMe(@AuthenticationPrincipal OAuth2User oAuth2User) {

        if (oAuth2User == null) {
            return null;
        }
        return userService.getUserByEmail(oAuth2User.getAttribute("email"));
    }
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/me")
    public UserDto updateMyName(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody String name) {
        if (oAuth2User == null) {
            return null;
        }
        return userService.updateUserName(oAuth2User.getAttribute("email"), name);
    }



}
