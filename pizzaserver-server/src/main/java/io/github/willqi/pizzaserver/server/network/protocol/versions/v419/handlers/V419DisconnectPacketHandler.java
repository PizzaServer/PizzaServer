package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.DisconnectPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

public class V419DisconnectPacketHandler extends ProtocolPacketHandler<DisconnectPacket> {

    @Override
    public void encode(DisconnectPacket packet, ByteBuf buffer) {
        buffer.writeBoolean(packet.isHidingDisconnectScreen());
        ByteBufUtility.writeString(packet.getKickMessage(), buffer);
    }

}

