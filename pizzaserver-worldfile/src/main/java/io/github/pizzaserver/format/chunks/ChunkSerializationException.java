package io.github.pizzaserver.format.chunks;

public class ChunkSerializationException extends RuntimeException {

    public ChunkSerializationException(String message) {
        super(message);
    }

    public ChunkSerializationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
