package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.network.protocol.packets.CreativeContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419CreativeContentPacketHandler extends BaseProtocolPacketHandler<CreativeContentPacket> {

    @Override
    public void encode(CreativeContentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getEntries().size());
        for (ItemStack entry : packet.getEntries()) {
            buffer.writeUnsignedVarInt(buffer.getVersion().getItemRuntimeId(entry.getItemType().getItemId()));
            buffer.writeItem(entry);
        }
    }

}
