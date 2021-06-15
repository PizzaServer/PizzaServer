package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.BiomeDefinitionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419BiomeDefinitionPacketHandler extends ProtocolPacketHandler<BiomeDefinitionPacket> {

    @Override
    public void encode(BiomeDefinitionPacket packet, ByteBuf buffer, PacketHelper helper) {
        helper.writeNBTCompound(packet.getTag(), buffer);
    }

}
