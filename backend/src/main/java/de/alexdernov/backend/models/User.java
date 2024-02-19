package de.alexdernov.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.security.AuthProvider;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public record User(
    @Id
    String id,
    String email,
    @DBRef
    List<Route> routes,
    AuthProvider authProvider

) {
    public User(String email) {
            this(UUID.randomUUID().toString(), email, Collections.emptyList(), AuthProvider.GOOGLE, Instant.now().truncatedTo(ChronoUnit.MILLIS));
        }

        public User withRoutes(List<Route> routesList) {
            return new User(id(), email(), routesList, authProvider());
        }

        public User withRoute(Route route) {
            List<Route> updatedRoutes = new LinkedList<>(routes());
            updatedRoutes.add(route);
            return withRoutes(updatedRoutes);
        }

        public User withoutGame(Game game) {
            List<Game> updatedGames = new ArrayList<>(games());
            updatedGames.remove(game);
            return withGames(updatedGames);
        }
}
