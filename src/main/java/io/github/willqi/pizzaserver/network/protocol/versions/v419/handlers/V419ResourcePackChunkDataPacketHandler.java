package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.network.protocol.packets.ResourcePackChunkDataPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

public class V419ResourcePackChunkDataPacketHandler implements ProtocolPacketHandler<ResourcePackChunkDataPacket> {

    @Override
    public ResourcePackChunkDataPacket decode(ByteBuf buffer) {
        return null;
    }

    @Override
    public void encode(ResourcePackChunkDataPacket packet, ByteBuf buffer) {
        ByteBufUtility.writeString(packet.getId() + "_" + packet.getVersion(), buffer);
        buffer.writeIntLE(packet.getChunkIndex());
        buffer.writeLongLE(packet.getChunkProgress());
        ByteBufUtility.writeByteArray(packet.getData(), buffer);
    }

}
