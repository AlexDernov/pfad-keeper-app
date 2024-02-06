package de.alexdernov.backend.models;


import java.time.LocalDateTime;

public record CoordsDto(
        LocalDateTime dateTime,
        String longitude,
        String latitude
) {
}
