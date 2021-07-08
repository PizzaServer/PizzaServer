package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePacksInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.packs.DataPack;
import io.netty.buffer.ByteBuf;

import java.util.Collection;

public class V419ResourcePacksInfoPacketHandler extends ProtocolPacketHandler<ResourcePacksInfoPacket> {

    @Override
    public void encode(ResourcePacksInfoPacket packet, ByteBuf buffer, PacketHelper helper) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isScriptingEnabled());
        writePacks(packet.getBehaviorPacks(), buffer, helper);
        writePacks(packet.getResourcePacks(), buffer, helper);
    }

    private static void writePacks(Collection<DataPack> packs, ByteBuf buffer, PacketHelper helper) {
        buffer.writeShortLE(packs.size());
        for (DataPack pack : packs) {
            helper.writeString(pack.getUuid().toString(), buffer);
            helper.writeString(pack.getVersion(), buffer);
            buffer.writeLongLE(pack.getDataLength());
            helper.writeString("", buffer);
            helper.writeString("", buffer);
            helper.writeString("", buffer);
            buffer.writeBoolean(false);
        }
    }
}
