package de.alexdernov.backend.models;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public record Coords(
        @Id
        String id,
        LocalDate date,
        String longitude,
        String latitude
) {
}
