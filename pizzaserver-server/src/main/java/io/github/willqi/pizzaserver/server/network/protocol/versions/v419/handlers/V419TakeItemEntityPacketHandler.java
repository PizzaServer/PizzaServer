package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.TakeItemEntityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419TakeItemEntityPacketHandler extends BaseProtocolPacketHandler<TakeItemEntityPacket> {

    @Override
    public void encode(TakeItemEntityPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getItemRuntimeEntityId());
        buffer.writeUnsignedVarLong(packet.getRuntimeEntityId());
    }

}
