package de.alexdernov.backend.models;

import java.util.List;

public record ImagesDto(
        List<Coords> coords,
        String url,
        String routeId
) {
}
