package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.packets.APIBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.BedrockPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for converting buffers to their packet form.
 */
public abstract class PacketRegistry {

    private final Map<Integer, ProtocolPacketHandler<? extends BedrockPacket>> handlers = new HashMap<>();

    public PacketRegistry register(int packetId, ProtocolPacketHandler<? extends BedrockPacket> handler) {
        this.handlers.put(packetId, handler);
        return this;
    }

    public PacketRegistry unregister(int packetId) {
        this.handlers.remove(packetId);
        return this;
    }

    public ProtocolPacketHandler<? extends APIBedrockPacket> getPacketHandler(int packetId) {
        return this.handlers.getOrDefault(packetId, null);
    }

    public abstract PacketHelper getPacketHelper();

}