package de.alexdernov.backend.models;

import org.springframework.data.annotation.Id;
import java.util.*;

public record User(
    @Id
    String id,
    String email,
    List<String> routeIds,
    AuthProvider authProvider

) {
    public User(String email) {
        this(UUID.randomUUID().toString(), email, Collections.emptyList(), AuthProvider.GOOGLE);
    }

    public User withRouteIds(List<String> updatedRouteIds) {
        return new User(this.id(), this.email(), updatedRouteIds, this.authProvider());
    }
}
