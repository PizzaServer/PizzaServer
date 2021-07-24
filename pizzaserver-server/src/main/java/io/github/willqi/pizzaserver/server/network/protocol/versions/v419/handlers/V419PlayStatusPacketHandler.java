package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayStatusPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419PlayStatusPacketHandler extends BaseProtocolPacketHandler<PlayStatusPacket> {

    @Override
    public void encode(PlayStatusPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        buffer.writeInt(packet.getStatus().ordinal());
    }

}
