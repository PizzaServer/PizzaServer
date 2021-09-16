package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackResponsePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ItemStackResponsePacketHandler extends BaseProtocolPacketHandler<ItemStackResponsePacket> {

    @Override
    public void encode(ItemStackResponsePacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getResponses().size());
        for (ItemStackResponsePacket.Response response : packet.getResponses()) {
            buffer.writeByte(response.getStatus().ordinal());
            buffer.writeVarInt(response.getRequestId());

            if (response.getStatus() == ItemStackResponsePacket.Response.Status.OK) {
                buffer.writeUnsignedVarInt(response.getInventories().size());

                response.getInventories().forEach((inventoryType, slots) -> {
                    buffer.writeByte(inventoryType.ordinal());

                    buffer.writeUnsignedVarInt(slots.size());
                    for (ItemStackResponsePacket.Response.SlotInfo slot : slots) {  // write all slots that changed
                        this.writeSlot(slot, buffer);
                    }
                });
            }
        }
    }

    protected void writeSlot(ItemStackResponsePacket.Response.SlotInfo slot, BasePacketBuffer buffer) {
        buffer.writeByte(slot.getSlot());
        buffer.writeByte(slot.getSlot());
        buffer.writeByte(slot.getItemStack().getCount());
        buffer.writeVarInt(slot.getItemStack().getNetworkId());
    }

}
