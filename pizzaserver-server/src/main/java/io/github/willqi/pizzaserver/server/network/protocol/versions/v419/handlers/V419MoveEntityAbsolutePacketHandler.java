package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.MoveEntityAbsolutePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419MoveEntityAbsolutePacketHandler extends BaseProtocolPacketHandler<MoveEntityAbsolutePacket> {

    @Override
    public void encode(MoveEntityAbsolutePacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        int flagByte = 0;
        for (MoveEntityAbsolutePacket.Flag flag : packet.getFlags()) {
            flagByte |= flag.ordinal() + 1;
        }
        buffer.writeByte(flagByte);
        buffer.writeVector3(packet.getPosition());
        buffer.writeByte((byte) (packet.getPitch() / (360f / 256f)));
        buffer.writeByte((byte) (packet.getYaw() / (360f / 256f)));
        buffer.writeByte((byte) (packet.getHeadYaw() / (360f / 256f)));
    }

}
