package de.alexdernov.backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;

@With
public record Images(
        @Id
        String id,
        Coords coords,
        String url,
        String routeId
) {
}
