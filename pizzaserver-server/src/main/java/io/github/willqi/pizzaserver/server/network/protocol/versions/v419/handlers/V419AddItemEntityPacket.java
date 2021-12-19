package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.AddItemEntityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419AddItemEntityPacket extends BaseProtocolPacketHandler<AddItemEntityPacket> {

    @Override
    public void encode(AddItemEntityPacket packet, BasePacketBuffer buffer) {
        buffer.writeVarLong(packet.getEntityUniqueId());
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        buffer.writeItem(packet.getItemStack());
        buffer.writeVector3(packet.getPosition());
        buffer.writeVector3(packet.getVelocity());
        buffer.writeEntityMetadata(packet.getMetaData());
        buffer.writeBoolean(packet.isFromFishing());
    }

}
