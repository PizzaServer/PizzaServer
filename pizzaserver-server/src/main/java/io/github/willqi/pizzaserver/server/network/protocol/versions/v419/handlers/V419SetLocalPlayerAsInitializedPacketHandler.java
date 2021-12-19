package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.SetLocalPlayerAsInitializedPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419SetLocalPlayerAsInitializedPacketHandler extends BaseProtocolPacketHandler<SetLocalPlayerAsInitializedPacket> {

    @Override
    public SetLocalPlayerAsInitializedPacket decode(BasePacketBuffer buffer) {
        SetLocalPlayerAsInitializedPacket packet = new SetLocalPlayerAsInitializedPacket();
        packet.setRuntimeEntityId(buffer.readUnsignedVarLong());
        return packet;
    }

}
