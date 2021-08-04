package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackChunkRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class V419ResourcePackChunkRequestPacketHandler extends BaseProtocolPacketHandler<ResourcePackChunkRequestPacket> {

    @Override
    public ResourcePackChunkRequestPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        ResourcePackChunkRequestPacket packet = new ResourcePackChunkRequestPacket();
        packet.setUUID(UUID.fromString(helper.readString(buffer)));
        packet.setChunkIndex(buffer.readIntLE());
        return packet;
    }

}
