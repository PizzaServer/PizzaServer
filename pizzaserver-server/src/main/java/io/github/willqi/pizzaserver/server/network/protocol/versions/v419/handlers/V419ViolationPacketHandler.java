package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ViolationPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ViolationPacketHandler extends BaseProtocolPacketHandler<ViolationPacket> {

    @Override
    public ViolationPacket decode(BasePacketBuffer buffer) {
        ViolationPacket packet = new ViolationPacket();
        packet.setType(buffer.readVarInt());
        packet.setSeverity(buffer.readVarInt());
        packet.setPacketId(buffer.readVarInt());
        packet.setMessage(buffer.readString());
        return packet;
    }

}
