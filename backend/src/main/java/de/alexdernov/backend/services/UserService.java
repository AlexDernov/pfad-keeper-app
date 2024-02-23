package de.alexdernov.backend.services;

import de.alexdernov.backend.models.User;
import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;



import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public UserDto getUserByEmail(String userEmail) {
        Optional<User> user = userRepo.getUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such email!");
        }
        return new UserDto(user.get().email(), user.get().name());
    }


    public UserDto updateUserName(String email, String userName) {
        Optional<User> optionalUser = userRepo.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            User user1 = userRepo.save(user.withName(userName));
            return new UserDto(user1.email(),user1.name());
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
