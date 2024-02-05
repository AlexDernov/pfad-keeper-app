package de.alexdernov.backend.models;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public record Route(
        @Id
        String id,
        List<Coords> coords,
        String name,
        LocalDate date
        ) {
}
