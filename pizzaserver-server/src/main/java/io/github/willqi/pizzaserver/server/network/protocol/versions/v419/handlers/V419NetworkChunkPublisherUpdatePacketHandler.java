package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.NetworkChunkPublisherUpdatePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419NetworkChunkPublisherUpdatePacketHandler extends BaseProtocolPacketHandler<NetworkChunkPublisherUpdatePacket> {

    @Override
    public void encode(NetworkChunkPublisherUpdatePacket packet, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeBlockVector(buffer, packet.getCoordinates());
        VarInts.writeUnsignedInt(buffer, packet.getRadius());
    }

}
