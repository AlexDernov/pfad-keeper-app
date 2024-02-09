package de.alexdernov.backend.models;

import java.time.LocalDateTime;
import java.util.List;

public record RouteDto(
        List<Coords> coords,
        String name,
        LocalDateTime dateTime
) {
}
