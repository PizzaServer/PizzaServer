package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419SetEntityDataPacketHandler extends BaseProtocolPacketHandler<SetEntityDataPacket> {

    @Override
    public void encode(SetEntityDataPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getRuntimeId());
        buffer.writeEntityMetadata(packet.getData());
        buffer.writeUnsignedVarLong(packet.getTick());
    }

}
