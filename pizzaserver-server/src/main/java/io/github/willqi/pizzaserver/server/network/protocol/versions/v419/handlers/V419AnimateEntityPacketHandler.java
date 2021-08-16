package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.AnimateEntityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.Set;

public class V419AnimateEntityPacketHandler extends BaseProtocolPacketHandler<AnimateEntityPacket> {

    @Override
    public void encode(AnimateEntityPacket packet, BasePacketBuffer buffer) {
        buffer.writeString(packet.getAnimation());
        buffer.writeString(packet.getNextState());
        buffer.writeString(packet.getStopExpression());
        buffer.writeString(packet.getController());
        buffer.writeFloatLE(packet.getBlendOutTime());
        Set<Long> entityRuntimeIDs = packet.getEntityRuntimeIDs();
        VarInts.writeUnsignedInt(buffer, entityRuntimeIDs.size());
        entityRuntimeIDs.forEach(entityRuntimeID -> VarInts.writeUnsignedLong(buffer, entityRuntimeID));
    }

}
