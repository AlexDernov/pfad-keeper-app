package de.alexdernov.backend.models;

import lombok.With;

public record UserDto(

        String email,
        @With
        String name
) {
    public UserDto(User user) {
        this(user.email(), user.name());
    }
}
