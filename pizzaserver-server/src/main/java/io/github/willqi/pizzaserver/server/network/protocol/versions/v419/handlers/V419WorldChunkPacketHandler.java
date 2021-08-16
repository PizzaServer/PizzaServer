package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldChunkPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419WorldChunkPacketHandler extends BaseProtocolPacketHandler<WorldChunkPacket> {

    @Override
    public void encode(WorldChunkPacket packet, BasePacketBuffer buffer) {
        buffer.writeVarInt(packet.getX());
        buffer.writeVarInt(packet.getZ());
        buffer.writeUnsignedVarInt(packet.getSubChunkCount());
        buffer.writeBoolean(false); // TODO: Implement chunk caching
        buffer.writeByteArray(packet.getData());
    }

}
