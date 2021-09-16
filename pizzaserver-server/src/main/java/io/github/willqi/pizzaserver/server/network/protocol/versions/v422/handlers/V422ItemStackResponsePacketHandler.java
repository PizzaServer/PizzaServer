package io.github.willqi.pizzaserver.server.network.protocol.versions.v422.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackResponsePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419ItemStackResponsePacketHandler;

public class V422ItemStackResponsePacketHandler extends V419ItemStackResponsePacketHandler {

    @Override
    protected void writeSlot(ItemStackResponsePacket.Response.SlotInfo slot, BasePacketBuffer buffer) {
        super.writeSlot(slot, buffer);
        buffer.writeString(slot.getItemStack().hasCustomName() ? slot.getItemStack().getCustomName() : "");
    }

}
