package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.DisconnectPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419DisconnectPacketHandler extends BaseProtocolPacketHandler<DisconnectPacket> {

    @Override
    public void encode(DisconnectPacket packet, BasePacketBuffer buffer) {
        buffer.writeBoolean(packet.isHidingDisconnectScreen());
        buffer.writeString(packet.getKickMessage());
    }

}

