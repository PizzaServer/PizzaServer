package io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.ItemStackResponsePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.handlers.V422ItemStackResponsePacketHandler;

public class V428ItemStackResponsePacketHandler extends V422ItemStackResponsePacketHandler {

    @Override
    protected void writeSlot(ItemStackResponsePacket.Response.SlotInfo slot, BasePacketBuffer buffer) {
        super.writeSlot(slot, buffer);
        buffer.writeVarInt(slot.getItemStack().getDamage());
    }

}
