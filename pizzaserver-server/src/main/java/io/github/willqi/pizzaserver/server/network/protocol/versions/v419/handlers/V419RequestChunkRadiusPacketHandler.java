package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.RequestChunkRadiusPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419RequestChunkRadiusPacketHandler extends BaseProtocolPacketHandler<RequestChunkRadiusPacket> {

    @Override
    public RequestChunkRadiusPacket decode(BasePacketBuffer buffer) {
        RequestChunkRadiusPacket packet = new RequestChunkRadiusPacket();
        packet.setChunkRadiusRequested(buffer.readVarInt());
        return packet;
    }

}
