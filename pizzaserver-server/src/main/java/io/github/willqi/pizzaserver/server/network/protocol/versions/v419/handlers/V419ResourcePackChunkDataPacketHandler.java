package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackChunkDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ResourcePackChunkDataPacketHandler extends BaseProtocolPacketHandler<ResourcePackChunkDataPacket> {

    @Override
    public void encode(ResourcePackChunkDataPacket packet, BasePacketBuffer buffer) {
        buffer.writeString(packet.getUUID().toString());
        buffer.writeIntLE(packet.getChunkIndex());
        buffer.writeLongLE(packet.getChunkProgress());
        buffer.writeByteArray(packet.getData());
    }

}
