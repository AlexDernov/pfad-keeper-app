package de.alexdernov.backend.models;

import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;


public record Coords(
        @Id
        String id,
        LocalDateTime dateTime,
        String longitude,
        String latitude
) {
}
