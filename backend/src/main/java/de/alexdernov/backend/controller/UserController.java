package de.alexdernov.backend.controller;

import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto getUser(@AuthenticationPrincipal OAuth2User user) {
        return userService.getLoggedInUserAsUserResponse(user);
    }
}
