package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackDataInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419ResourcePackDataInfoPacketHandler extends BaseProtocolPacketHandler<ResourcePackDataInfoPacket> {

    @Override
    public void encode(ResourcePackDataInfoPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeString(packet.getPackId() + "_" + packet.getVersion(), buffer);
        buffer.writeIntLE(packet.getMaxChunkSize());
        buffer.writeIntLE(packet.getChunkCount());
        buffer.writeLongLE(packet.getCompressedPackageSize());
        helper.writeByteArray(packet.getHash(), buffer);
        buffer.writeBoolean(packet.isPremium());
        buffer.writeByte(packet.getType().ordinal());
    }
}
