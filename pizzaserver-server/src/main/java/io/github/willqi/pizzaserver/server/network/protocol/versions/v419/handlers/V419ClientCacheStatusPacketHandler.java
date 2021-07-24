package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ClientCacheStatusPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419ClientCacheStatusPacketHandler extends BaseProtocolPacketHandler<ClientCacheStatusPacket> {

    @Override
    public ClientCacheStatusPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        ClientCacheStatusPacket packet = new ClientCacheStatusPacket();
        packet.setSupported(buffer.readBoolean());
        return packet;
    }

    @Override
    public void encode(ClientCacheStatusPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        buffer.writeBoolean(packet.isSupported());
    }

}
