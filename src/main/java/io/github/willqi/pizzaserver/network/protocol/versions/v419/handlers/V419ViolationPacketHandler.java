package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.network.protocol.packets.ViolationPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

public class V419ViolationPacketHandler implements ProtocolPacketHandler<ViolationPacket> {

    @Override
    public ViolationPacket decode(ByteBuf buffer) {
        ViolationPacket packet = new ViolationPacket();
        packet.setType(VarInts.readInt(buffer));
        packet.setSeverity(VarInts.readInt(buffer));
        packet.setPacketId(VarInts.readInt(buffer));
        packet.setMessage(ByteBufUtility.readString(buffer));
        return packet;
    }

    @Override
    public void encode(ViolationPacket packet, ByteBuf buffer) {

    }

}
