package io.github.willqi.pizzaserver.server.network.protocol.versions.v431;

import io.github.willqi.pizzaserver.api.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.InventorySlotPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.InventoryTransactionPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.V428PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431InventoryContentPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431InventorySlotPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431InventoryTransactionPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431WorldSoundEventPacketHandler;

public class V431PacketRegistry extends V428PacketRegistry {

    public V431PacketRegistry() {
        this.register(WorldSoundEventPacket.ID, new V431WorldSoundEventPacketHandler())
            .register(InventoryContentPacket.ID, new V431InventoryContentPacketHandler())
            .register(InventorySlotPacket.ID, new V431InventorySlotPacketHandler())
            .register(InventoryTransactionPacket.ID, new V431InventoryTransactionPacketHandler());
    }

}
