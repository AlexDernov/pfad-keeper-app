package de.alexdernov.backend.models;

public record UserDto(
        String id,
        String email,
        String name
) {
    public UserDto(User user) {
        this(user.id(), user.email(), user.name());
    }
}
