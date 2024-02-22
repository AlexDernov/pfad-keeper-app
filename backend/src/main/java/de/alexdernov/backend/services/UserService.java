package de.alexdernov.backend.services;

import de.alexdernov.backend.models.User;
import de.alexdernov.backend.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserByEmail(String userEmail) {
        Optional<User> user = userRepo.getUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such email!");
        }
        return new User(user.get().id(), user.get().email(), user.get().routeIds(), user.get().name(), user.get().authProvider());
    }

    public User getUserByName(String name) {
        Optional<User> user = userRepo.getUserByName(name);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such name!");
        }
        return new User(user.get().id(), user.get().email(), user.get().routeIds(), user.get().name(), user.get().authProvider());
    }

    public User updateUsersRouteIdsList(String email, String routeId) {
        Optional<User> optionalUser = userRepo.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<String> updatedRouteIds = new ArrayList<>(user.routeIds());
            if (updatedRouteIds.contains(routeId)) {
                updatedRouteIds.remove(routeId);
            } else {
                updatedRouteIds.add(routeId);
            }
            User updatedUser = user.withRouteIds(updatedRouteIds);
            return userRepo.save(updatedUser);
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such email!"));
    }
    public User updateUserName(String email, String userName) {
        Optional<User> optionalUser = userRepo.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return userRepo.save(user.withName(userName));
        }
        throw (new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such email!"));
    }

    public boolean saveNewUser(OAuth2User oAuth2User) {
        String userEmail = oAuth2User.getAttribute("email");
        String name = "";
        if (userEmail == null || userEmail.isEmpty()) {
            return false;
        }

        boolean isReturningUser = userRepo.existsByEmail(userEmail.trim());

        if (!isReturningUser) {
            User newUser = new User(userEmail.trim(), name);
            userRepo.save(newUser);
        }

        return true;
    }


}
