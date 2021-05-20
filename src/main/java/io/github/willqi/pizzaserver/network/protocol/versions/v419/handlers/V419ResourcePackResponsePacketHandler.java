package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.network.protocol.packets.ResourcePackResponsePacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class V419ResourcePackResponsePacketHandler implements ProtocolPacketHandler<ResourcePackResponsePacket> {

    @Override
    public ResourcePackResponsePacket decode(ByteBuf buffer) {
        ResourcePackResponsePacket packet = new ResourcePackResponsePacket();
        packet.setStatus(ResourcePackResponsePacket.ResourcePackStatus.values()[buffer.readUnsignedByte()]);

        PackInfo[] packs = new PackInfo[buffer.readShortLE()];
        for (int i = 0; i < packs.length; i++) {
            String[] data = ByteBufUtility.readString(buffer).split("_");
            packs[i] = new PackInfo(UUID.fromString(data[0]), data[1]);
        }
        packet.setPacksRequested(packs);

        return packet;
    }

    @Override
    public void encode(ResourcePackResponsePacket packet, ByteBuf buffer) {

    }

}
