package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ChunkRadiusUpdatedPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ChunkRadiusUpdatedPacketHandler extends BaseProtocolPacketHandler<ChunkRadiusUpdatedPacket> {

    @Override
    public void encode(ChunkRadiusUpdatedPacket packet, BasePacketBuffer buffer) {
        buffer.writeVarInt(packet.getRadius());
    }

}
