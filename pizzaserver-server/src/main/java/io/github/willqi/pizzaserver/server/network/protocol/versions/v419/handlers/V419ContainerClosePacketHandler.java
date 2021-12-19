package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.ContainerClosePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ContainerClosePacketHandler extends BaseProtocolPacketHandler<ContainerClosePacket> {

    @Override
    public void encode(ContainerClosePacket packet, BasePacketBuffer buffer) {
        buffer.writeByte(packet.getInventoryId());
        buffer.writeBoolean(packet.isClosedByServer());
    }

    @Override
    public ContainerClosePacket decode(BasePacketBuffer buffer) {
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.setInventoryId(buffer.readByte());
        containerClosePacket.setClosedByServer(buffer.readBoolean());
        return containerClosePacket;
    }
}
