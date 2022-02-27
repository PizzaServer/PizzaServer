package io.github.pizzaserver.format.api.chunks;

public class ChunkSerializationException extends RuntimeException {

    public ChunkSerializationException(String message) {
        super(message);
    }

    public ChunkSerializationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
