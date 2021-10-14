package io.github.willqi.pizzaserver.server.network.protocol.versions.v431;

import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.V428PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.*;

public class V431PacketRegistry extends V428PacketRegistry {

    public V431PacketRegistry() {
        this.register(WorldSoundEventPacket.ID, new V431WorldSoundEventPacketHandler())
            .register(InventoryContentPacket.ID, new V431InventoryContentPacketHandler())
            .register(InventorySlotPacket.ID, new V431InventorySlotPacketHandler())
            .register(InventoryTransactionPacket.ID, new V431InventoryTransactionPacketHandler())
            .register(WorldEventPacket.ID, new V431WorldEventPacketHandler());
    }

}
