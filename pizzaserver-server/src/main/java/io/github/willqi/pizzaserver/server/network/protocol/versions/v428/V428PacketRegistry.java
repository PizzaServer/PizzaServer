package io.github.willqi.pizzaserver.server.network.protocol.versions.v428;

import io.github.willqi.pizzaserver.api.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.V422PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.*;

public class V428PacketRegistry extends V422PacketRegistry {

    public V428PacketRegistry() {
        this.register(StartGamePacket.ID, new V428StartGamePacketHandler())
            .register(PlayerActionPacket.ID, new V428PlayerActionPacketHandler())
            .register(WorldSoundEventPacket.ID, new V428WorldSoundEventPacketHandler())
            .register(ItemStackResponsePacket.ID, new V428ItemStackResponsePacketHandler())
            .register(WorldEventPacket.ID, new V428WorldEventPacketHandler());
    }

}
