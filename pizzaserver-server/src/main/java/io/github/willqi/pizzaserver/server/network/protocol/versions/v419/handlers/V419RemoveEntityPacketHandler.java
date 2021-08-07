package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.RemoveEntityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419RemoveEntityPacketHandler extends BaseProtocolPacketHandler<RemoveEntityPacket> {

    @Override
    public void encode(RemoveEntityPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
    }

}
