package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.RequestChunkRadiusPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419RequestChunkRadiusPacketHandler extends ProtocolPacketHandler<RequestChunkRadiusPacket> {

    @Override
    public RequestChunkRadiusPacket decode(ByteBuf buffer, PacketHelper helper) {
        RequestChunkRadiusPacket packet = new RequestChunkRadiusPacket();
        packet.setChunkRadiusRequested(VarInts.readInt(buffer));
        return packet;
    }

}
