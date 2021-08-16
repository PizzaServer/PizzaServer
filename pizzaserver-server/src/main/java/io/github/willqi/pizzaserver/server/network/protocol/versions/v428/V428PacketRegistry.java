package io.github.willqi.pizzaserver.server.network.protocol.versions.v428;

import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerActionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.V422PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428PlayerActionPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428SetEntityDataPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428StartGamePacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428WorldSoundEventPacketHandler;

public class V428PacketRegistry extends V422PacketRegistry {

    public V428PacketRegistry() {
        this.register(StartGamePacket.ID, new V428StartGamePacketHandler())
            .register(PlayerActionPacket.ID, new V428PlayerActionPacketHandler())
            .register(SetEntityDataPacket.ID, new V428SetEntityDataPacketHandler())
            .register(WorldSoundEventPacket.ID, new V428WorldSoundEventPacketHandler());
    }

}
