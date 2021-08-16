package io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428StartGamePacketHandler;

public class V440StartGamePacketHandler extends V428StartGamePacketHandler {

    @Override
    public void encode(StartGamePacket packet, BasePacketBuffer buffer) {
        super.encode(packet, buffer);
        buffer.writeString(packet.getServerEngine());
    }

}
