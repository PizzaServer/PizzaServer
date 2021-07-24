package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerActionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419PlayerActionPacketHandler extends ProtocolPacketHandler<PlayerActionPacket> {
    @Override
    public PlayerActionPacket decode(ByteBuf buffer, PacketHelper helper) {
        PlayerActionPacket packet = new PlayerActionPacket();
        packet.setEntityRuntimeID(VarInts.readUnsignedLong(buffer));
        packet.setActionType(VarInts.readInt(buffer));
        packet.setVector3(new Vector3(
                VarInts.readInt(buffer),
                VarInts.readUnsignedInt(buffer),
                VarInts.readInt(buffer)
        ));
        packet.setFace(VarInts.readInt(buffer));
        return packet;
    }
}
