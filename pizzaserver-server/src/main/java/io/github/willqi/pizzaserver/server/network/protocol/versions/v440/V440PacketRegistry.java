package io.github.willqi.pizzaserver.server.network.protocol.versions.v440;

import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.V431PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers.V440SetEntityDataPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers.V440StartGamePacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers.V440WorldSoundEventPacketHandler;

public class V440PacketRegistry extends V431PacketRegistry {

    public V440PacketRegistry() {
        this.register(WorldSoundEventPacket.ID, new V440WorldSoundEventPacketHandler())
            .register(SetEntityDataPacket.ID, new V440SetEntityDataPacketHandler())
            .register(StartGamePacket.ID, new V440StartGamePacketHandler());
    }

}
