package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419InventoryContentPacketHandler extends BaseProtocolPacketHandler<InventoryContentPacket> {

    @Override
    public void encode(InventoryContentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getInventoryId());
        buffer.writeUnsignedVarInt(packet.getContents().size());
        for (NetworkItemStackData networkItemStackData : packet.getContents()) {
            // Write the unique stack id before the item (or zero if it is empty) (used for authoritative server inventory system)
            buffer.writeVarInt(networkItemStackData.getRuntimeId() == 0 ? 0 : networkItemStackData.getItemStack().getStackId());
            buffer.writeItem(networkItemStackData);
        }
    }

}
