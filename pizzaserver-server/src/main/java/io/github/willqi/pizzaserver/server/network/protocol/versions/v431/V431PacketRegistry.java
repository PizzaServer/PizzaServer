package io.github.willqi.pizzaserver.server.network.protocol.versions.v431;

import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.V428PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431WorldSoundEventPacketHandler;

public class V431PacketRegistry extends V428PacketRegistry {

    public V431PacketRegistry() {
        this.register(WorldSoundEventPacket.ID, new V431WorldSoundEventPacketHandler());
    }

}
