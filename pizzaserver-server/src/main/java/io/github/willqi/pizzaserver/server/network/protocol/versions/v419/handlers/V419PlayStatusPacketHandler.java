package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.PlayStatusPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419PlayStatusPacketHandler extends BaseProtocolPacketHandler<PlayStatusPacket> {

    @Override
    public void encode(PlayStatusPacket packet, BasePacketBuffer buffer) {
        buffer.writeInt(packet.getStatus().ordinal());
    }

}
