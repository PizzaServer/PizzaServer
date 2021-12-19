package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.SetEntityVelocityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419SetEntityVelocityPacketHandler extends BaseProtocolPacketHandler<SetEntityVelocityPacket> {

    @Override
    public void encode(SetEntityVelocityPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        buffer.writeVector3(packet.getVelocity());
    }

}
