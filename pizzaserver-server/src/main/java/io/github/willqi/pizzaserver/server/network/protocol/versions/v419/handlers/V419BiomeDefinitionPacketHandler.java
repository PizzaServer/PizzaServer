package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419BiomeDefinitionPacketHandler extends ProtocolPacketHandler<BedrockPacket> {

    @Override
    public void encode(BedrockPacket packet, ByteBuf buffer, PacketHelper helper) {

    }

}
