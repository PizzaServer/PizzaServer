package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for converting buffers to their packet form.
 */
public abstract class BasePacketRegistry {

    private final Map<Integer, BaseProtocolPacketHandler<? extends BaseBedrockPacket>> handlers = new HashMap<>();

    public BasePacketRegistry register(int packetId, BaseProtocolPacketHandler<? extends BaseBedrockPacket> handler) {
        this.handlers.put(packetId, handler);
        return this;
    }

    public BasePacketRegistry unregister(int packetId) {
        this.handlers.remove(packetId);
        return this;
    }

    public BaseProtocolPacketHandler<? extends BaseBedrockPacket> getPacketHandler(int packetId) {
        return this.handlers.getOrDefault(packetId, null);
    }

    public abstract BasePacketHelper getPacketHelper();

}