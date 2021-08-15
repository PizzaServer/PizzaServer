package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePacksInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;

import java.util.Collection;

public class V419ResourcePacksInfoPacketHandler extends BaseProtocolPacketHandler<ResourcePacksInfoPacket> {

    @Override
    public void encode(ResourcePacksInfoPacket packet, BasePacketBuffer buffer) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isScriptingEnabled());
        writePacks(packet.getBehaviorPacks(), buffer);
        writePacks(packet.getResourcePacks(), buffer);
    }

    private static void writePacks(Collection<ResourcePack> packs, BasePacketBuffer buffer) {
        buffer.writeShortLE(packs.size());
        for (ResourcePack pack : packs) {
            buffer.writeString(pack.getUuid().toString());
            buffer.writeString(pack.getVersion());
            buffer.writeLongLE(pack.getDataLength());
            buffer.writeString("");
            buffer.writeString("");
            buffer.writeString("");
            buffer.writeBoolean(false);
        }
    }
}
