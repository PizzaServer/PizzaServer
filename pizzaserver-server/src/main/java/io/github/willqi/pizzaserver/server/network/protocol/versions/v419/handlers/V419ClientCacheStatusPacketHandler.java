package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ClientCacheStatusPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ClientCacheStatusPacketHandler extends BaseProtocolPacketHandler<ClientCacheStatusPacket> {

    @Override
    public ClientCacheStatusPacket decode(BasePacketBuffer buffer) {
        ClientCacheStatusPacket packet = new ClientCacheStatusPacket();
        packet.setSupported(buffer.readBoolean());
        return packet;
    }

    @Override
    public void encode(ClientCacheStatusPacket packet, BasePacketBuffer buffer) {
        buffer.writeBoolean(packet.isSupported());
    }

}
