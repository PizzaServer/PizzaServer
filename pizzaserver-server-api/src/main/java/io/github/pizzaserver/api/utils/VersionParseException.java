package io.github.pizzaserver.api.utils;

public class VersionParseException extends IllegalArgumentException {
    public VersionParseException() {
    }

    public VersionParseException(String s) {
        super(s);
    }

    public VersionParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionParseException(Throwable cause) {
        super(cause);
    }
}
