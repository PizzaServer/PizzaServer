package io.github.willqi.pizzaserver.server.network.protocol.versions.v422.handlers;

import io.github.willqi.pizzaserver.api.packs.ResourcePack;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419ResourcePacksInfoPacketHandler;

import java.util.Collection;

public class V422ResourcePacksInfoPacketHandler extends V419ResourcePacksInfoPacketHandler {

    @Override
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
            buffer.writeBoolean(pack.isRayTracingEnabled());
        }
    }

}
