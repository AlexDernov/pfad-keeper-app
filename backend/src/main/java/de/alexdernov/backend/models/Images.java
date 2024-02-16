package de.alexdernov.backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.List;
@With
public record Images(
        @Id
        String id,
        List<Coords> coords,
        String url,
        String routeId
) {
}
