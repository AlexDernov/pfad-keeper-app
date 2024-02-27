package de.alexdernov.backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.*;

public record User(
        @Id
        String id,
        String email,
        @With
        String name

) {
    public User(String email, String name) {
        this(UUID.randomUUID().toString(), email, name);
    }

}
