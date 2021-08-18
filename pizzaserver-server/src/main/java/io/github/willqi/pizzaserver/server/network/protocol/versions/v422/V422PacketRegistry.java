package io.github.willqi.pizzaserver.server.network.protocol.versions.v422;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePacksInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.handlers.V422ResourcePacksInfoPacketHandler;

public class V422PacketRegistry extends V419PacketRegistry {

    public V422PacketRegistry() {
        this.register(ResourcePacksInfoPacket.ID, new V422ResourcePacksInfoPacketHandler());
    }

}
