package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerHotbarPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419PlayerHotbarPacketHandler extends BaseProtocolPacketHandler<PlayerHotbarPacket> {

    @Override
    public void encode(PlayerHotbarPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getHotbarSlot());
        buffer.writeByte(packet.getInventoryId());
        buffer.writeBoolean(packet.shouldSelectSlot());
    }

}
