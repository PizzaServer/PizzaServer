package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.AnimateEntityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419AnimateEntityPacketHandler extends BaseProtocolPacketHandler<AnimateEntityPacket> {
    @Override
    public void encode(AnimateEntityPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeString(packet.getAnimation(), buffer);
        helper.writeString(packet.getNextState(), buffer);
        helper.writeString(packet.getStopExpression(), buffer);
        helper.writeString(packet.getController(), buffer);
        buffer.writeFloatLE(packet.getBlendOutTime());
        VarInts.writeUnsignedInt(buffer, packet.getEntityArraySize().intValue());
        VarInts.writeUnsignedLong(buffer, packet.getEntityRuntimeID().longValue());
    }
}
