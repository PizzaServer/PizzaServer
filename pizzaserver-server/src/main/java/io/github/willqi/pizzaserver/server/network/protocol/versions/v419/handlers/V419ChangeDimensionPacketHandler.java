package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.ChangeDimensionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ChangeDimensionPacketHandler extends BaseProtocolPacketHandler<ChangeDimensionPacket> {

    @Override
    public void encode(ChangeDimensionPacket packet, BasePacketBuffer buffer) {
        buffer.writeVarInt(packet.getDimension().ordinal());
        buffer.writeVector3(packet.getPosition());
        buffer.writeBoolean(packet.isRespawnResponse());
    }

}
