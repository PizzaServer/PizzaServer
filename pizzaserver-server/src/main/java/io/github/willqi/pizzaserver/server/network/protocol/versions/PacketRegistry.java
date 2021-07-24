package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.BedrockNetworkPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for converting buffers to their packet form.
 */
public abstract class PacketRegistry {

    private final Map<Integer, ProtocolPacketHandler<? extends BedrockNetworkPacket>> handlers = new HashMap<>();

    public PacketRegistry register(int packetId, ProtocolPacketHandler<? extends BedrockNetworkPacket> handler) {
        this.handlers.put(packetId, handler);
        return this;
    }

    public PacketRegistry unregister(int packetId) {
        this.handlers.remove(packetId);
        return this;
    }

    public ProtocolPacketHandler<? extends BedrockPacket> getPacketHandler(int packetId) {
        return this.handlers.getOrDefault(packetId, null);
    }

    public abstract PacketHelper getPacketHelper();

}