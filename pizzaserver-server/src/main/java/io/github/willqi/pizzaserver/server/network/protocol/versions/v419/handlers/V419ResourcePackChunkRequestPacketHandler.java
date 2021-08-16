package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackChunkRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.UUID;

public class V419ResourcePackChunkRequestPacketHandler extends BaseProtocolPacketHandler<ResourcePackChunkRequestPacket> {

    @Override
    public ResourcePackChunkRequestPacket decode(BasePacketBuffer buffer) {
        ResourcePackChunkRequestPacket packet = new ResourcePackChunkRequestPacket();
        packet.setUUID(UUID.fromString(buffer.readString()));
        packet.setChunkIndex(buffer.readIntLE());
        return packet;
    }

}
