package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.server.network.protocol.data.TeleportationCause;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MovePlayerPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class V419MovePlayerPacketHandler extends BaseProtocolPacketHandler<MovePlayerPacket> {

    @Override
    public MovePlayerPacket decode(BasePacketBuffer buffer) {
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();

        movePlayerPacket.setEntityRuntimeId(buffer.readUnsignedVarLong());
        movePlayerPacket.setPosition(buffer.readVector3());
        Vector3 rotation = buffer.readVector3();
        movePlayerPacket.setPitch(rotation.getX());
        movePlayerPacket.setYaw(rotation.getY());
        movePlayerPacket.setHeadYaw(rotation.getZ());
        movePlayerPacket.setMode(MovementMode.values()[buffer.readByte()]);
        movePlayerPacket.setOnGround(buffer.readBoolean());
        movePlayerPacket.setRidingEntityRuntimeId(buffer.readUnsignedVarLong());

        if (movePlayerPacket.getMode() == MovementMode.TELEPORT) {
            movePlayerPacket.setTeleportationCause(TeleportationCause.values()[buffer.readIntLE()]);
            movePlayerPacket.setEntityType(buffer.readIntLE());
        }
        movePlayerPacket.setTick(buffer.readUnsignedVarLong());

        return movePlayerPacket;
    }

    @Override
    public void encode(MovePlayerPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        buffer.writeVector3(packet.getPosition());
        buffer.writeVector3(new Vector3(packet.getPitch(), packet.getYaw(), packet.getHeadYaw()));
        buffer.writeByte(packet.getMode().ordinal());
        buffer.writeBoolean(packet.isOnGround());
        buffer.writeUnsignedVarLong(packet.getRidingEntityRuntimeId());

        if (packet.getMode() == MovementMode.TELEPORT) {
            buffer.writeIntLE(packet.getTeleportationCause().ordinal());
            buffer.writeIntLE(packet.getEntityType());
        }
        buffer.writeUnsignedVarLong(packet.getTick());
    }

}
