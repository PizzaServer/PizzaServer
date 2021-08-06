package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419SetEntityDataPacketHandler extends BaseProtocolPacketHandler<SetEntityDataPacket> {

    @Override
    public void encode(SetEntityDataPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeId());
        helper.writeEntityMetadata(packet.getData(), buffer);
        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

}
