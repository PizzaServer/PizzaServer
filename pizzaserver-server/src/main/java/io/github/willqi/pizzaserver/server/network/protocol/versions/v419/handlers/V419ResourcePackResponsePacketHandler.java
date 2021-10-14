package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.api.network.protocol.packets.ResourcePackResponsePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class V419ResourcePackResponsePacketHandler extends BaseProtocolPacketHandler<ResourcePackResponsePacket> {

    @Override
    public ResourcePackResponsePacket decode(BasePacketBuffer buffer) {
        ResourcePackResponsePacket packet = new ResourcePackResponsePacket();
        packet.setStatus(ResourcePackResponsePacket.ResourcePackStatus.values()[buffer.readUnsignedByte()]);

        int amountOfPacks = buffer.readShortLE();
        Set<PackInfo> packs = new HashSet<>();

        for (int i = 0; i < amountOfPacks; i++) {
            String[] data = buffer.readString().split("_");
            packs.add(new PackInfo(UUID.fromString(data[0]), data[1]));
        }
        packet.setPacksRequested(packs);

        return packet;
    }

}
