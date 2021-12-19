package io.github.willqi.pizzaserver.server.network.protocol.versions.v448;

import io.github.willqi.pizzaserver.api.network.protocol.packets.ResourcePacksInfoPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.WorldEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.V440PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v448.handlers.V448ResourcePacksInfoPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v448.handlers.V448WorldEventPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v448.handlers.V448WorldSoundEventPacketHandler;

public class V448PacketRegistry extends V440PacketRegistry {

    public V448PacketRegistry() {
        this.register(WorldSoundEventPacket.ID, new V448WorldSoundEventPacketHandler())
            .register(WorldEventPacket.ID, new V448WorldEventPacketHandler())
            .register(ResourcePacksInfoPacket.ID, new V448ResourcePacksInfoPacketHandler());
    }

}
