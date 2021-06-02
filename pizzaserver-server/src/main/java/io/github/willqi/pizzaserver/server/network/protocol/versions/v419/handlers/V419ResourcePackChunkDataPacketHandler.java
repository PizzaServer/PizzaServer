package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackChunkDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

public class V419ResourcePackChunkDataPacketHandler extends ProtocolPacketHandler<ResourcePackChunkDataPacket> {

    @Override
    public void encode(ResourcePackChunkDataPacket packet, ByteBuf buffer) {
        ByteBufUtility.writeString(packet.getId() + "_" + packet.getVersion(), buffer);
        buffer.writeIntLE(packet.getChunkIndex());
        buffer.writeLongLE(packet.getChunkProgress());
        ByteBufUtility.writeByteArray(packet.getData(), buffer);
    }

}
