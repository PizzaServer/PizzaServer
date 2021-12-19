package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.SetPlayerGamemodePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419SetPlayerGamemodePacketHandler extends BaseProtocolPacketHandler<SetPlayerGamemodePacket> {

    @Override
    public void encode(SetPlayerGamemodePacket packet, BasePacketBuffer buffer) {
        buffer.writeVarInt(packet.getGamemode().ordinal());
    }

}
