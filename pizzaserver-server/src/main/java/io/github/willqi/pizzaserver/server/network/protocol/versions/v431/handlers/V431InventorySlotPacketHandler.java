package io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419InventorySlotPacketHandler;

public class V431InventorySlotPacketHandler extends V419InventorySlotPacketHandler {

    @Override
    protected void writeItemStack(ItemStack itemStack, BasePacketBuffer buffer) {
        buffer.writeItem(itemStack);
    }
    
}
