package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419InventoryContentPacketHandler extends BaseProtocolPacketHandler<InventoryContentPacket> {

    @Override
    public void encode(InventoryContentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getInventoryId());
        buffer.writeUnsignedVarInt(packet.getContents().length);
        for (ItemStack itemStack : packet.getContents()) {
            this.writeItemStack(itemStack, buffer);
        }
    }

    protected void writeItemStack(ItemStack itemStack, BasePacketBuffer buffer) {
        buffer.writeVarInt(itemStack.getNetworkId());
        buffer.writeItem(itemStack);
    }

}
