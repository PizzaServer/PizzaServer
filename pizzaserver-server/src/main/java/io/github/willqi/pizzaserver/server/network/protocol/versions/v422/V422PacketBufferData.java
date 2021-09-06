package io.github.willqi.pizzaserver.server.network.protocol.versions.v422;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryActionType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419PacketBufferData;

public class V422PacketBufferData extends V419PacketBufferData {

    public static final BasePacketBufferData INSTANCE = new V422PacketBufferData();


    public V422PacketBufferData() {
        this.registerInventoryActionType(InventoryActionType.CRAFT_RECIPE_OPTIONAL, 12)
            .registerInventoryActionType(InventoryActionType.CRAFT_NOT_IMPLEMENTED, 13)
            .registerInventoryActionType(InventoryActionType.CRAFT_RESULTS_DEPRECATED, 14);
    }

}
