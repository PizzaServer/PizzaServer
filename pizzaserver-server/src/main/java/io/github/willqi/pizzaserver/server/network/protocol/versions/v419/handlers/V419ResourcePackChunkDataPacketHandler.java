package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackChunkDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419ResourcePackChunkDataPacketHandler extends BaseProtocolPacketHandler<ResourcePackChunkDataPacket> {

    @Override
    public void encode(ResourcePackChunkDataPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeString(packet.getId() + "_" + packet.getVersion(), buffer);
        buffer.writeIntLE(packet.getChunkIndex());
        buffer.writeLongLE(packet.getChunkProgress());
        helper.writeByteArray(packet.getData(), buffer);
    }

}
