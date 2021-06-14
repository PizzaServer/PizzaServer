package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackResponsePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class V419ResourcePackResponsePacketHandler extends ProtocolPacketHandler<ResourcePackResponsePacket> {

    @Override
    public ResourcePackResponsePacket decode(ByteBuf buffer, PacketHelper helper) {
        ResourcePackResponsePacket packet = new ResourcePackResponsePacket();
        packet.setStatus(ResourcePackResponsePacket.ResourcePackStatus.values()[buffer.readUnsignedByte()]);

        PackInfo[] packs = new PackInfo[buffer.readShortLE()];
        for (int i = 0; i < packs.length; i++) {
            String[] data = helper.readString(buffer).split("_");
            packs[i] = new PackInfo(UUID.fromString(data[0]), data[1]);
        }
        packet.setPacksRequested(packs);

        return packet;
    }

}
