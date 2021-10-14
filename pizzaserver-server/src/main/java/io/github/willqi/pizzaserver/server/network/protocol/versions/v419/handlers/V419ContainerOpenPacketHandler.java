package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.ContainerOpenPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ContainerOpenPacketHandler extends BaseProtocolPacketHandler<ContainerOpenPacket> {

    @Override
    public void encode(ContainerOpenPacket packet, BasePacketBuffer buffer) {
        buffer.writeByte(packet.getInventoryId());
        buffer.writeByte(packet.getInventoryType().getId());
        buffer.writeVector3i(packet.getCoordinates());
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
    }

}
