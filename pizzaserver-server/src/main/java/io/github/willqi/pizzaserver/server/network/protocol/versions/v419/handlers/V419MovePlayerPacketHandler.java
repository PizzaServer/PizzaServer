package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.server.network.protocol.data.TeleportationCause;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MovePlayerPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.netty.buffer.ByteBuf;

public class V419MovePlayerPacketHandler extends BaseProtocolPacketHandler<MovePlayerPacket> {

    @Override
    public MovePlayerPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();

        movePlayerPacket.setEntityRuntimeId(VarInts.readUnsignedLong(buffer));
        movePlayerPacket.setPosition(helper.readVector3(buffer));
        Vector3 rotation = helper.readVector3(buffer);
        movePlayerPacket.setPitch(rotation.getX());
        movePlayerPacket.setYaw(rotation.getY());
        movePlayerPacket.setHeadYaw(rotation.getZ());
        movePlayerPacket.setMode(MovementMode.values()[buffer.readByte()]);
        movePlayerPacket.setOnGround(buffer.readBoolean());
        movePlayerPacket.setRidingEntityRuntimeId(VarInts.readUnsignedLong(buffer));

        if (movePlayerPacket.getMode() == MovementMode.TELEPORT) {
            movePlayerPacket.setTeleportationCause(TeleportationCause.values()[buffer.readIntLE()]);
            movePlayerPacket.setEntityType(buffer.readIntLE());
        }
        movePlayerPacket.setTick(VarInts.readUnsignedLong(buffer));

        return movePlayerPacket;
    }

    @Override
    public void encode(MovePlayerPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeUnsignedLong(buffer, packet.getEntityRuntimeId());
        helper.writeVector3(buffer, packet.getPosition());
        helper.writeVector3(buffer, new Vector3(packet.getPitch(), packet.getYaw(), packet.getHeadYaw()));
        buffer.writeByte(packet.getMode().ordinal());
        buffer.writeBoolean(packet.isOnGround());
        VarInts.writeUnsignedLong(buffer, packet.getRidingEntityRuntimeId());

        if (packet.getMode() == MovementMode.TELEPORT) {
            buffer.writeIntLE(packet.getTeleportationCause().ordinal());
            buffer.writeIntLE(packet.getEntityType());
        }
        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

}
