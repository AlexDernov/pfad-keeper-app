package de.alexdernov.backend.services;

import de.alexdernov.backend.models.User;
import de.alexdernov.backend.models.UserDto;
import de.alexdernov.backend.repos.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;

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
    void getAllUsers_WhenRepoEmpty_ReturnsEmptyList() {
        // GIVEN
        UserRepo userRepoMock = mock(UserRepo.class);
        when(userRepoMock.findAll()).thenReturn(new ArrayList<>());
        UserService userService = new UserService(userRepoMock);

        // WHEN
        List<User> result = userService.getAllUsers();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUsers_WhenRepoHasData_ReturnsListOfUsers() {
        // GIVEN
        UserRepo userRepoMock = mock(UserRepo.class);
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User("user1", "email1@example.com"));
        mockUsers.add(new User("user2", "email2@example.com"));
        when(userRepoMock.findAll()).thenReturn(mockUsers);
        UserService userService = new UserService(userRepoMock);

        // WHEN
        List<User> result = userService.getAllUsers();

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());

    }
    @Test
    void updateUserName_WhenUserExists_ReturnsUpdatedUserDto() {
        // GIVEN
        UserRepo userRepoMock = mock(UserRepo.class);
        User existingUser = new User("test@example.com", "OldName");
        when(userRepoMock.getUserByEmail("test@example.com")).thenReturn(Optional.of(existingUser));
        when(userRepoMock.save(any(User.class))).thenReturn(existingUser.withName("NewName"));
        UserService userService = new UserService(userRepoMock);

        // WHEN
        UserDto result = userService.updateUserName("test@example.com", "NewName");

        // THEN
        assertNotNull(result);
        assertEquals("test@example.com", result.email());
        assertEquals("NewName", result.name());
    }

    @Test
    void updateUserName_WhenUserDoesNotExist_ThrowsResponseStatusException() {
        // GIVEN
        UserRepo userRepoMock = mock(UserRepo.class);
        when(userRepoMock.getUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        UserService userService = new UserService(userRepoMock);

        // WHEN and THEN
        assertThrows(ResponseStatusException.class, () -> {
            userService.updateUserName("nonexistent@example.com", "NewName");
        });
    }

    @Test
    void getUserByEmailTest_whenEmail_thenReturnUserWithTheEmail() {
        //GIVEN
        String expectedEmail = "Email";
        Mockito.when(userRepo.getUserByEmail(expectedEmail)).thenReturn(Optional.of(new User("Email", "Name")));

        UserService userService = new UserService(userRepo);
        //WHEN
        UserDto actual = userService.getUserByEmail(expectedEmail);

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
        // GIVEN
        String emptyUserEmail = "";
        OAuth2User oauth2User = mock(OAuth2User.class);
        when(oauth2User.getAttribute("email")).thenReturn(emptyUserEmail);

        // WHEN
        boolean actual = userService.saveNewUser(oauth2User);

        // THEN
        assertFalse(actual);
    }
}