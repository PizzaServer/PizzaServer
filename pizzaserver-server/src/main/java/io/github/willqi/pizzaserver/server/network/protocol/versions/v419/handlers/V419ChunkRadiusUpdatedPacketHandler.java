package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ChunkRadiusUpdatedPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419ChunkRadiusUpdatedPacketHandler extends ProtocolPacketHandler<ChunkRadiusUpdatedPacket> {

    @Override
    public void encode(ChunkRadiusUpdatedPacket packet, ByteBuf buffer, PacketHelper helper) {
        VarInts.writeInt(buffer, packet.getRadius());
    }

}
