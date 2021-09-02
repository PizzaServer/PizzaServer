package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventorySlotPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419InventorySlotPacketHandler extends BaseProtocolPacketHandler<InventorySlotPacket> {

    @Override
    public void encode(InventorySlotPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getInventoryId());
        buffer.writeUnsignedVarInt(packet.getSlot());
        this.writeItemStack(packet.getItem(), buffer);
    }

    protected void writeItemStack(ItemStack itemStack, BasePacketBuffer buffer) {
        buffer.writeVarInt(itemStack.getNetworkId());
        buffer.writeItem(itemStack);
    }

}
