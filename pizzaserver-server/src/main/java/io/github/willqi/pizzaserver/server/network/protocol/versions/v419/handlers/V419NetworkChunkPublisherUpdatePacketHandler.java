package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.NetworkChunkPublisherUpdatePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419NetworkChunkPublisherUpdatePacketHandler extends BaseProtocolPacketHandler<NetworkChunkPublisherUpdatePacket> {

    @Override
    public void encode(NetworkChunkPublisherUpdatePacket packet, BasePacketBuffer buffer) {
        buffer.writeVector3i(packet.getCoordinates());
        buffer.writeUnsignedVarInt(packet.getRadius());
    }

}
