package io.github.pizzaserver.server.network.protocol.exception;

import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;

/**
 * Thrown when a protocol operation is performed that is not supported by the minecraft version.
 */
public class OutdatedProtocolException extends ProtocolException {

    public OutdatedProtocolException(MinecraftVersion version, String message) {
        super(version, message);
    }
}
