package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.SetTimePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419SetTimePacketHandler extends BaseProtocolPacketHandler<SetTimePacket> {

    @Override
    public void encode(SetTimePacket packet, BasePacketBuffer buffer) {
        buffer.writeVarInt(packet.getTime());
    }

}
