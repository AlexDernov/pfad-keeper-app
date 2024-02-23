package de.alexdernov.backend.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserDtoTest {

    @Test
    void testUserDtoConstructor() {
        // GIVEN
        String userEmail = "test@example.com";
        String userName = "Test User";

        // WHEN
        User user = new User(userEmail, userName);
        UserDto userDto = new UserDto(user);

        // THEN
        Assertions.assertEquals(userEmail, userDto.email());
        Assertions.assertEquals(userName, userDto.name());
    }

}