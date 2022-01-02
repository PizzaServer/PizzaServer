package io.github.pizzaserver.server.network.protocol.exception;

import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;

/**
 * General exception thrown when a protocol exception occurs.
 */
public class ProtocolException extends RuntimeException {

    public ProtocolException(MinecraftVersion version, String message) {
        super(String.format("[v%s] %s", version.getProtocol(), message));
    }

}
