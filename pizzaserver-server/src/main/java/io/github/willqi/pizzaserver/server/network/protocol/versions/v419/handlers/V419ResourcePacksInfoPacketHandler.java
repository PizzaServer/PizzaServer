package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.ResourcePacksInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;

import java.util.Collection;

public class V419ResourcePacksInfoPacketHandler extends BaseProtocolPacketHandler<ResourcePacksInfoPacket> {

    @Override
    public void encode(ResourcePacksInfoPacket packet, BasePacketBuffer buffer) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isScriptingEnabled());
        this.writeBehaviourPacks(packet.getBehaviorPacks(), buffer);
        this.writeResourcePacks(packet.getResourcePacks(), buffer);
    }

    protected void writeResourcePacks(Collection<ResourcePack> packs, BasePacketBuffer buffer) {
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

    protected void writeBehaviourPacks(Collection<ResourcePack> packs, BasePacketBuffer buffer) {
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
