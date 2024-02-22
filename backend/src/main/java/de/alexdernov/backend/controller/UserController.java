package de.alexdernov.backend.controller;


import de.alexdernov.backend.models.User;
import de.alexdernov.backend.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public User getMe(@AuthenticationPrincipal OAuth2User oAuth2User) {

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
    public User updateMyName(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody String encodedName) {
        String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
        if (oAuth2User == null) {
            return null;
        }
        return userService.updateUserName(oAuth2User.getAttribute("email"), name);
    }

    @PostMapping("/{routeId}")
    public User updateRouteIdsList( @PathVariable String routeId, @RequestBody String email) {
        return userService.updateUsersRouteIdsList( email, routeId);
    }

    @DeleteMapping("/{routeId}")
    public User updateRouteIdsList( @PathVariable String routeId, @RequestBody String email) {
        return userService.updateUsersRouteIdsList( email, routeId);
    }

}
