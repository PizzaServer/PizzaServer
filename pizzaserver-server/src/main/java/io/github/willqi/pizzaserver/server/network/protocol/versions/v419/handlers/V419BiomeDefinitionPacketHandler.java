package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BiomeDefinitionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419BiomeDefinitionPacketHandler extends BaseProtocolPacketHandler<BiomeDefinitionPacket> {

    @Override
    public void encode(BiomeDefinitionPacket packet, BasePacketBuffer buffer) {
        buffer.writeNBTCompound(packet.getTag());
    }

}
