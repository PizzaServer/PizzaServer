package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayStatusPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419PlayStatusPacketHandler extends ProtocolPacketHandler<PlayStatusPacket> {

    @Override
    public void encode(PlayStatusPacket packet, ByteBuf buffer) {
        buffer.writeInt(packet.getStatus().ordinal());
    }

}