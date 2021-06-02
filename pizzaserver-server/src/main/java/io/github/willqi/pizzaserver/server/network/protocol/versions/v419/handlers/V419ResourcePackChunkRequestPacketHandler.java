package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackChunkRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class V419ResourcePackChunkRequestPacketHandler extends ProtocolPacketHandler<ResourcePackChunkRequestPacket> {

    @Override
    public ResourcePackChunkRequestPacket decode(ByteBuf buffer) {
        String[] packHeader = ByteBufUtility.readString(buffer).split("_");
        int index = buffer.readIntLE();

        ResourcePackChunkRequestPacket packet = new ResourcePackChunkRequestPacket();
        packet.setPackInfo(new PackInfo(UUID.fromString(packHeader[0]), packHeader[1]));
        packet.setChunkIndex(index);
        return packet;
    }

}
