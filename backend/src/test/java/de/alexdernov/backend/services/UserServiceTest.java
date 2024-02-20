package de.alexdernov.backend.services;

import de.alexdernov.backend.models.AuthProvider;
import de.alexdernov.backend.models.Coords;
import de.alexdernov.backend.models.Image;
import de.alexdernov.backend.models.User;
import de.alexdernov.backend.repos.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private final UserRepo userRepo = mock(UserRepo.class);
    private UserService userService;
    private final String userEmail = "user@example.com";
    private final String userName = "";

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepo);
    }

    @Test
    void getUserByEmailTest_whenEmail_thenReturnUserWithTheEmail() {
        //GIVEN
        String expectedEmail = "Email";
        Mockito.when(userRepo.getUserByEmail(expectedEmail)).thenReturn(Optional.of(new User("1", "Email", new ArrayList<>(List.of("1", "2")), "Name", AuthProvider.GOOGLE)));

        UserService userService = new UserService(userRepo);
        //WHEN
        User actual = userService.getUserByEmail(expectedEmail);

        //THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedEmail, actual.email());
    }

    @Test
    void getUserByEmailTest_whenIdNotFound_thenThrowResponseStatusException() {
        // GIVEN
        String expectedEmail = "nonExistentEmail";
        Mockito.when(userRepo.getUserByEmail(expectedEmail)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResponseStatusException.class, () -> userService.getUserByEmail(expectedEmail));
    }

    @Test
    void getUserByNameTest_whenNameExists_thenReturnUserWithTheName() {
        //GIVEN
        String expectedName = "Name";
        Mockito.when(userRepo.getUserByName(expectedName)).thenReturn(Optional.of(new User("1", "Email", new ArrayList<>(List.of("1", "2")), "Name", AuthProvider.GOOGLE)));

        UserService userService = new UserService(userRepo);
        //WHEN
        User actual = userService.getUserByName(expectedName);

        //THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedName, actual.name());
    }

    @Test
    void getUserByNameTest_whenIdNotFound_thenThrowResponseStatusException() {
        // GIVEN
        String expectedName = "nonExistentName";
        Mockito.when(userRepo.getUserByName(expectedName)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResponseStatusException.class, () -> userService.getUserByName(expectedName));
    }

    @Test
    void updateRouteIdsTest_whenRouteIdNew_returnUpdatedList() {
        //GIVEN
        String routeId = "3";
        Mockito.when(userRepo.getUserByEmail("Email")).thenReturn(Optional.of(new User("1", "Email", new ArrayList<>(List.of("1", "2")), "Name", AuthProvider.GOOGLE)));
        User userToUpdate = (new User("1", "Email", new ArrayList<>(List.of("1", "2", routeId)), "Name", AuthProvider.GOOGLE));
        Mockito.when((userRepo.save(Mockito.any()))).thenReturn(userToUpdate);

        UserService userService = new UserService(userRepo);
        //WHEN
        User actual = userService.updateRouteIds(userToUpdate.email(), routeId);

        //THEN
        assertEquals(userToUpdate, actual);
        Mockito.verify(userRepo, Mockito.times(1)).save(userToUpdate);
    }

    @Test
    void updateRouteIdsTest_whenRouteIdAlreadyExists_RemoveFromList() {
        //GIVEN
        String existingBookId = "2";
        Mockito.when(userRepo.getUserByEmail("Email")).thenReturn(Optional.of(new User("1", "Email", new ArrayList<>(List.of("1", "2")), "Name", AuthProvider.GOOGLE)));
        User userToUpdate = (new User("1", "Email", new ArrayList<>(List.of("1", "2", existingBookId)), "Name", AuthProvider.GOOGLE));
        Mockito.when((userRepo.save(Mockito.any()))).thenReturn(userToUpdate);

        UserService userService = new UserService(userRepo);
        //WHEN
        User actual = userService.updateRouteIds(userToUpdate.email(), existingBookId);

        //THEN
        assertFalse(actual.routeIds().contains(existingBookId));
        Mockito.verify(userRepo, Mockito.times(1)).save(actual);
    }

    @Test
    void saveNewUserTest_whenUserNotExists_thenSaveNewUser() {
        // GIVEN
        OAuth2User oauth2User = mock(OAuth2User.class);
        when(oauth2User.getAttribute("email")).thenReturn(userEmail);
        when(userRepo.existsByEmail(userEmail)).thenReturn(false);

        User testUser = new User(userEmail, userName);
        when(userRepo.save(testUser)).thenReturn(testUser);

        // WHEN
        boolean actual = userService.saveNewUser(oauth2User);

        // THEN
        assertTrue(actual);
        verify(userRepo).existsByEmail(userEmail);
        verify(userRepo).save(any(User.class));
        verifyNoMoreInteractions(userRepo);
    }
    @Test
    void saveNewUserTest_whenUserExists_thenReturnTrue() {
        // GIVEN
        OAuth2User oauth2User = mock(OAuth2User.class);
        when(oauth2User.getAttribute("email")).thenReturn(userEmail);
        when(userRepo.existsByEmail(userEmail)).thenReturn(true);

        // WHEN
        boolean actual = userService.saveNewUser(oauth2User);

        // THEN
        assertTrue(actual);
        verify(userRepo).existsByEmail(userEmail);
        verifyNoMoreInteractions(userRepo);
    }

    @Test
    void saveNewUserTest_whenOAuthUserHasNoEmail_thenReturnFalse() {
        // ARRANGE
        String emptyUserEmail = "";
        OAuth2User oauth2User = mock(OAuth2User.class);
        when(oauth2User.getAttribute("email")).thenReturn(emptyUserEmail);

        // ACT
        boolean actual = userService.saveNewUser(oauth2User);

        // ASSERT
        assertFalse(actual);
    }
}