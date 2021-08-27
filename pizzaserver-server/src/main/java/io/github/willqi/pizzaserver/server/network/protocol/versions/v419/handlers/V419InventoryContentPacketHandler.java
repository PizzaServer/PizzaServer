package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419InventoryContentPacketHandler extends BaseProtocolPacketHandler<InventoryContentPacket> {

    @Override
    public void encode(InventoryContentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getInventoryId());
        buffer.writeUnsignedVarInt(packet.getContents().length);
        for (ItemStack itemStack : packet.getContents()) {
            // Write the unique stack id before the item (or zero if it is empty) (used for authoritative server inventory system)
            // However, it is not needed and writing a 1 in its place is fine as the packets tell us the slots involved.
            buffer.writeVarInt(buffer.getVersion().getItemRuntimeId(itemStack.getItemType().getItemId()) == 0 ? 0 : 1);
            buffer.writeItem(itemStack);
        }
    }

}
