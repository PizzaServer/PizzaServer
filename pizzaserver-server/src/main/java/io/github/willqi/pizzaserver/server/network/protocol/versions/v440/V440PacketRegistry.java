package io.github.willqi.pizzaserver.server.network.protocol.versions.v440;

import io.github.willqi.pizzaserver.server.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.V431PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers.V440StartGamePacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers.V440WorldEventPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers.V440WorldSoundEventPacketHandler;

public class V440PacketRegistry extends V431PacketRegistry {

    public V440PacketRegistry() {
        this.register(WorldSoundEventPacket.ID, new V440WorldSoundEventPacketHandler())
            .register(WorldEventPacket.ID, new V440WorldEventPacketHandler())
            .register(StartGamePacket.ID, new V440StartGamePacketHandler());
    }

}
