package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackChunkRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class V419ResourcePackChunkRequestPacketHandler extends BaseProtocolPacketHandler<ResourcePackChunkRequestPacket> {

    @Override
    public ResourcePackChunkRequestPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        String[] packHeader = helper.readString(buffer).split("_");
        int index = buffer.readIntLE();

        ResourcePackChunkRequestPacket packet = new ResourcePackChunkRequestPacket();
        packet.setPackInfo(new PackInfo(UUID.fromString(packHeader[0]), packHeader[1]));
        packet.setChunkIndex(index);
        return packet;
    }

}
