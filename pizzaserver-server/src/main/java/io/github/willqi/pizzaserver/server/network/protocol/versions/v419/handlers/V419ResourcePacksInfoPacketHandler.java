package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePacksInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.utils.ByteBufUtility;
import io.github.willqi.pizzaserver.server.packs.DataPack;
import io.netty.buffer.ByteBuf;

public class V419ResourcePacksInfoPacketHandler implements ProtocolPacketHandler<ResourcePacksInfoPacket> {
    @Override
    public ResourcePacksInfoPacket decode(ByteBuf buffer) {
        return null;
    }

    @Override
    public void encode(ResourcePacksInfoPacket packet, ByteBuf buffer) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isScriptingEnabled());
        writePacks(packet.getBehaviorPacks(), buffer);
        writePacks(packet.getResourcePacks(), buffer);
    }

    private static void writePacks(DataPack[] packs, ByteBuf buffer) {
        buffer.writeShortLE(packs.length);
        for (DataPack pack : packs) {
            ByteBufUtility.writeString(pack.getUuid().toString(), buffer);
            ByteBufUtility.writeString(pack.getVersion(), buffer);
            buffer.writeLongLE(pack.getDataLength());
            ByteBufUtility.writeString("", buffer);
            ByteBufUtility.writeString("", buffer);
            ByteBufUtility.writeString("", buffer);
            buffer.writeBoolean(false);
        }
    }
}
