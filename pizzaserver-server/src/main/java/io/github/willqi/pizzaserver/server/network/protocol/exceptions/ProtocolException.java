package io.github.willqi.pizzaserver.server.network.protocol.exceptions;

import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;

/**
 * General exception thrown when a protocol exception occurs.
 */
public class ProtocolException extends RuntimeException {

    public ProtocolException(MinecraftVersion version, String message) {
        super(String.format("[v%s] %s", version.getProtocol(), message));
    }

}
