package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.RemoveEntityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419RemoveEntityPacketHandler extends BaseProtocolPacketHandler<RemoveEntityPacket> {

    @Override
    public void encode(RemoveEntityPacket packet, BasePacketBuffer buffer) {
        buffer.writeVarLong(packet.getUniqueEntityId());
    }

}
