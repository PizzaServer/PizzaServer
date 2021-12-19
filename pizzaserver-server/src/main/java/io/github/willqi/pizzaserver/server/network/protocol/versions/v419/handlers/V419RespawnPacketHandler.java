package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.RespawnPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419RespawnPacketHandler extends BaseProtocolPacketHandler<RespawnPacket> {

    @Override
    public RespawnPacket decode(BasePacketBuffer buffer) {
        RespawnPacket respawnPacket = new RespawnPacket();
        respawnPacket.setPosition(buffer.readVector3());
        respawnPacket.setState(RespawnPacket.State.values()[buffer.readByte()]);
        respawnPacket.setEntityRuntimeId(buffer.readUnsignedVarLong());
        return respawnPacket;
    }

    @Override
    public void encode(RespawnPacket packet, BasePacketBuffer buffer) {
        buffer.writeVector3(packet.getPosition());
        buffer.writeByte(packet.getState().ordinal());
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
    }

}
