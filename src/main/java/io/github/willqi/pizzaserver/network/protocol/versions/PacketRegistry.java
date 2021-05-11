package io.github.willqi.pizzaserver.network.protocol.versions;

import io.github.willqi.pizzaserver.network.protocol.packets.BedrockPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for converting buffers to their packet form.
 */
public abstract class PacketRegistry {

    private final Map<Integer, PacketHandler<? extends BedrockPacket>> handlers = new HashMap<>();

    public PacketRegistry register(int packetId, PacketHandler<? extends BedrockPacket> handler) {
        this.handlers.put(packetId, handler);
        return this;
    }

    public PacketRegistry unregister(int packetId) {
        this.handlers.remove(packetId);
        return this;
    }

    public PacketHandler<? extends BedrockPacket> getPacketHandler(int packetId) {
        return this.handlers.getOrDefault(packetId, null);
    }

}