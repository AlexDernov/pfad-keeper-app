package de.alexdernov.backend.models;

import java.time.LocalDate;

public record CoordsDto(
        LocalDate date,
        String longitude,
        String latitude
) {
}
