package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ImplBedrockPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for converting buffers to their packet form.
 */
public abstract class BasePacketRegistry {

    private final Map<Integer, BaseProtocolPacketHandler<? extends ImplBedrockPacket>> handlers = new HashMap<>();

    public BasePacketRegistry register(int packetId, BaseProtocolPacketHandler<? extends ImplBedrockPacket> handler) {
        this.handlers.put(packetId, handler);
        return this;
    }

    public BasePacketRegistry unregister(int packetId) {
        this.handlers.remove(packetId);
        return this;
    }

    public BaseProtocolPacketHandler<? extends BedrockPacket> getPacketHandler(int packetId) {
        return this.handlers.getOrDefault(packetId, null);
    }

    public abstract BasePacketHelper getPacketHelper();

}