package de.alexdernov.backend.models;

import java.time.LocalDate;
import java.util.List;

public record RouteDto(
        List<Coords> coords,
        String name,
        LocalDate date
) {
}
