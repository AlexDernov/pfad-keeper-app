package de.alexdernov.backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.*;

public record User(
        @Id
        String id,
        String email,
        @With
        List<String> routeIds,
        @With
        String name,
        AuthProvider authProvider

) {
    public User(String email, String name) {
        this(UUID.randomUUID().toString(), email, Collections.emptyList(), name, AuthProvider.GOOGLE);
    }

    public User withRouteIds(List<String> updatedRouteIds) {
        return new User(this.id(), this.email(), updatedRouteIds, this.name(), this.authProvider());
    }
}
