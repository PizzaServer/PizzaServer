package io.github.willqi.pizzaserver.server.network.protocol.versions.v448.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePacksInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.handlers.V422ResourcePacksInfoPacketHandler;

public class V448ResourcePacksInfoPacketHandler extends V422ResourcePacksInfoPacketHandler {

    @Override
    public void encode(ResourcePacksInfoPacket packet, BasePacketBuffer buffer) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isScriptingEnabled());
        buffer.writeBoolean(packet.isForcingServerPacks());
        this.writeBehaviourPacks(packet.getBehaviorPacks(), buffer);
        this.writeResourcePacks(packet.getResourcePacks(), buffer);
    }

}
