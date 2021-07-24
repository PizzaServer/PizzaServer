package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetLocalPlayerAsInitializedPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419SetLocalPlayerAsInitializedPacketHandler extends BaseProtocolPacketHandler<SetLocalPlayerAsInitializedPacket> {

    @Override
    public SetLocalPlayerAsInitializedPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        SetLocalPlayerAsInitializedPacket packet = new SetLocalPlayerAsInitializedPacket();
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        return packet;
    }

}
