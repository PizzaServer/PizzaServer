package io.github.pizzaserver.format.api.chunks;

import java.io.IOException;

public class ChunkParseException extends IOException {

    public ChunkParseException(String message) {
        super(message);
    }

    public ChunkParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
