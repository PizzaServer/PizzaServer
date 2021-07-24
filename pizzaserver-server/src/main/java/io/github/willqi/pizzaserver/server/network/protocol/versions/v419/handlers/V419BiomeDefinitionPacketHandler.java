package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.BiomeDefinitionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419BiomeDefinitionPacketHandler extends BaseProtocolPacketHandler<BiomeDefinitionPacket> {

    @Override
    public void encode(BiomeDefinitionPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeNBTCompound(packet.getTag(), buffer);
    }

}
