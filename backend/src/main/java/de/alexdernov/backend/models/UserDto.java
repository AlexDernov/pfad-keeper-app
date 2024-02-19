package de.alexdernov.backend.models;

public record UserDto(
        String id,
        String email
) {
    public UserDto(User user) {
        this(user.id(), user.email());
    }
}
