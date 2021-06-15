package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LevelChunkPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419LevelChunkPacketHandler extends ProtocolPacketHandler<LevelChunkPacket> {

    @Override
    public void encode(LevelChunkPacket packet, ByteBuf buffer, PacketHelper helper) {
        VarInts.writeInt(buffer, packet.getX());
        VarInts.writeInt(buffer, packet.getZ());
        VarInts.writeUnsignedInt(buffer, packet.getSubChunkCount());
        buffer.writeBoolean(false); // TODO: Implement chunk caching
        helper.writeByteArray(packet.getData(), buffer);
    }

}
