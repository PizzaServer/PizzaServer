package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419SetEntityDataPacketHandler extends ProtocolPacketHandler<SetEntityDataPacket> {

    @Override
    public void encode(SetEntityDataPacket packet, ByteBuf buffer, PacketHelper helper) {
        
    }

}
