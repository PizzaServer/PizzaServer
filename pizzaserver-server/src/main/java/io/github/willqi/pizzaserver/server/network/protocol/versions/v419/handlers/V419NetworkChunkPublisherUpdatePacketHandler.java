package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.NetworkChunkPublisherUpdatePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419NetworkChunkPublisherUpdatePacketHandler extends ProtocolPacketHandler<NetworkChunkPublisherUpdatePacket> {

    @Override
    public void encode(NetworkChunkPublisherUpdatePacket packet, ByteBuf buffer, PacketHelper helper) {
        VarInts.writeInt(buffer, packet.getCoordinates().getX());
        VarInts.writeUnsignedInt(buffer, packet.getCoordinates().getY());
        VarInts.writeInt(buffer, packet.getCoordinates().getZ());

        VarInts.writeUnsignedInt(buffer, packet.getRadius());
    }

}
