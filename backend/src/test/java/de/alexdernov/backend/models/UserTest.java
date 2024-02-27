package de.alexdernov.backend.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



class UserTest {

    @Test
    void testUserConstructor() {
        // GIVEN
        String expectedId = "123";
        String expectedEmail = "test@example.com";
        String expectedName = "Test User";

        // WHEN
        User user = new User(expectedId, expectedEmail, expectedName);

        // THEN
        Assertions.assertEquals(expectedId, user.id());
        Assertions.assertEquals(expectedEmail, user.email());
        Assertions.assertEquals(expectedName, user.name());
    }

    @Test
    void testUserTwoArgConstructor() {
        // GIVEN
        String expectedEmail = "test@example.com";
        String expectedName = "Test User";

        // WHEN
        User user = new User(expectedEmail, expectedName);

        // THEN
        Assertions.assertNotNull(user.id());
        Assertions.assertEquals(expectedEmail, user.email());
        Assertions.assertEquals(expectedName, user.name());

    }

}