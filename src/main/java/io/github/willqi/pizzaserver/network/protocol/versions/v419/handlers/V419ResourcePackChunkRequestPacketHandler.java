package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.network.protocol.packets.ResourcePackChunkRequestPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class V419ResourcePackChunkRequestPacketHandler implements ProtocolPacketHandler<ResourcePackChunkRequestPacket> {

    @Override
    public ResourcePackChunkRequestPacket decode(ByteBuf buffer) {
        String[] packHeader = ByteBufUtility.readString(buffer).split("_");
        int index = buffer.readIntLE();

        ResourcePackChunkRequestPacket packet = new ResourcePackChunkRequestPacket();
        packet.setPackInfo(new PackInfo(UUID.fromString(packHeader[0]), packHeader[1]));
        packet.setChunkIndex(index);
        return packet;
    }

    @Override
    public void encode(ResourcePackChunkRequestPacket packet, ByteBuf buffer) {

    }

}
