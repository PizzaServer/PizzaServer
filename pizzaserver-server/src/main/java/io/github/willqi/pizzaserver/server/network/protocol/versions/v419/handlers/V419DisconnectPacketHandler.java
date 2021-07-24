package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.DisconnectPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419DisconnectPacketHandler extends BaseProtocolPacketHandler<DisconnectPacket> {

    @Override
    public void encode(DisconnectPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        buffer.writeBoolean(packet.isHidingDisconnectScreen());
        helper.writeString(packet.getKickMessage(), buffer);
    }

}

