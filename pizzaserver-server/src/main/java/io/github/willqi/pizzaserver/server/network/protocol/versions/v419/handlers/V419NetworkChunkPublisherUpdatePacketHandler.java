package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.NetworkChunkPublisherUpdatePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419NetworkChunkPublisherUpdatePacketHandler extends BaseProtocolPacketHandler<NetworkChunkPublisherUpdatePacket> {

    @Override
    public void encode(NetworkChunkPublisherUpdatePacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeInt(buffer, packet.getCoordinates().getX());
        VarInts.writeUnsignedInt(buffer, packet.getCoordinates().getY());
        VarInts.writeInt(buffer, packet.getCoordinates().getZ());

        VarInts.writeUnsignedInt(buffer, packet.getRadius());
    }

}
