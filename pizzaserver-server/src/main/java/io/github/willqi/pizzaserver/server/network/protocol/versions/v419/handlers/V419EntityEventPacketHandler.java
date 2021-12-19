package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.EntityEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.exceptions.ProtocolException;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419EntityEventPacketHandler extends BaseProtocolPacketHandler<EntityEventPacket> {

    @Override
    public void encode(EntityEventPacket packet, BasePacketBuffer buffer) {
        if (!buffer.getData().isEntityEventSupported(packet.getType())) {
            throw new ProtocolException(buffer.getVersion(), String.format("%s is not a supported entity event.", packet.getType()));
        }

        buffer.writeUnsignedVarLong(packet.getRuntimeEntityId());
        buffer.writeByte(buffer.getData().getEntityEventId(packet.getType()));
        buffer.writeVarInt(packet.getData());
    }

}
