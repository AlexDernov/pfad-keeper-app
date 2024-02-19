package de.alexdernov.backend.services;

import de.alexdernov.backend.exceptions.GoogleEmailNotFoundException;
import de.alexdernov.backend.models.User;
import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepo userRepo;


    public void save(User user) {
        userRepo.save(user);
    }

    public User getLoggedInUser(OAuth2User user) {
        if (user == null) {
            return null;
        }

        String userEmail = user.getAttribute("email");

        if (userEmail == null || userEmail.isEmpty()) {
            throw new GoogleEmailNotFoundException("Email must be present to proceed.");
        }

        userEmail = userEmail.trim();

        return userRepo.getUserByEmail(userEmail);
    }

    public UserDto getLoggedInUserAsUserResponse(OAuth2User user) {
        User loggedInUser = getLoggedInUser(user);
        if (loggedInUser == null) {
            return null;
        }
        return new UserDto(loggedInUser);
    }


    public boolean saveNewUser(OAuth2User oAuth2User) {
        String userEmail = oAuth2User.getAttribute("email");

        if (userEmail == null || userEmail.isEmpty()) {
            return false;
        }

        boolean isReturningUser = userRepo.existsByEmail(userEmail.trim());

        if (!isReturningUser) {
            User newUser = new User(userEmail.trim());
            userRepo.save(newUser);
        }

        return true;
    }

    public List<String> getAllRoutes(String userId) {
        return userRepo.findById(userId).orElseThrow(NoSuchElementException::new).routeIds();
    }

    public User removeRoute(User user, String routeId) {
        List<String> updatedRouteIds = new ArrayList<>(user.routeIds());
        updatedRouteIds.remove(routeId);
        User updatedUser = user.withRouteIds(updatedRouteIds);
        return userRepo.save(updatedUser);
    }
}
