package io.github.willqi.pizzaserver.mcworld.exceptions.world.chunks;

import java.io.IOException;

/**
 * Called when there is no chunk found
 */
public class NoChunkFoundException extends IOException {

    public NoChunkFoundException(String message) {
        super(message);
    }

}
