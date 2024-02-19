package de.alexdernov.backend.exceptions;

public class GoogleEmailNotFoundException extends RuntimeException {
    public GoogleEmailNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
