package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LevelChunkPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419LevelChunkPacketHandler extends BaseProtocolPacketHandler<LevelChunkPacket> {

    @Override
    public void encode(LevelChunkPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeInt(buffer, packet.getX());
        VarInts.writeInt(buffer, packet.getZ());
        VarInts.writeUnsignedInt(buffer, packet.getSubChunkCount());
        buffer.writeBoolean(false); // TODO: Implement chunk caching
        helper.writeByteArray(packet.getData(), buffer);
    }

}
