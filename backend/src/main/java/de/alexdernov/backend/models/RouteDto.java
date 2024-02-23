package de.alexdernov.backend.models;

import java.time.LocalDateTime;
import java.util.List;

public record RouteDto(
        List<Coords> coords,
        List<UserDto> members,
        String name,
        LocalDateTime dateTime
) {
}
