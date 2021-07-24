package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ViolationPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419ViolationPacketHandler extends BaseProtocolPacketHandler<ViolationPacket> {

    @Override
    public ViolationPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        ViolationPacket packet = new ViolationPacket();
        packet.setType(VarInts.readInt(buffer));
        packet.setSeverity(VarInts.readInt(buffer));
        packet.setPacketId(VarInts.readInt(buffer));
        packet.setMessage(helper.readString(buffer));
        return packet;
    }

}
