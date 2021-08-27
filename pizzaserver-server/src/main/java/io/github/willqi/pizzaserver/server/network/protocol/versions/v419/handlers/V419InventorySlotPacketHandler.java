package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.InventorySlotPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419InventorySlotPacketHandler extends BaseProtocolPacketHandler<InventorySlotPacket> {

    @Override
    public void encode(InventorySlotPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getInventoryId());
        buffer.writeUnsignedVarInt(packet.getSlot());
        buffer.writeVarInt(buffer.getVersion().getItemRuntimeId(packet.getItem().getItemType().getItemId()) == 0 ? 0 : 1);
        buffer.writeItem(packet.getItem());
    }

}
