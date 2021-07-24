package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ChunkRadiusUpdatedPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419ChunkRadiusUpdatedPacketHandler extends BaseProtocolPacketHandler<ChunkRadiusUpdatedPacket> {

    @Override
    public void encode(ChunkRadiusUpdatedPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeInt(buffer, packet.getRadius());
    }

}
