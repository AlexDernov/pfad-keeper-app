package de.alexdernov.backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
@With
public record Route(
        @Id
        String id,
        List<Coords> coords,
        List<UserDto> userIds,
        String name,
        LocalDateTime dateTime
) {
}
