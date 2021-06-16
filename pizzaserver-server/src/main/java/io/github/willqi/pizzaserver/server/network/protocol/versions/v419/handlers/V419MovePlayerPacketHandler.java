package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.server.network.protocol.data.TeleportationCause;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MovePlayerPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.utils.Vector3;
import io.netty.buffer.ByteBuf;

public class V419MovePlayerPacketHandler extends ProtocolPacketHandler<MovePlayerPacket> {

    @Override
    public MovePlayerPacket decode(ByteBuf buffer, PacketHelper helper) {
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();

        movePlayerPacket.setEntityRuntimeId(VarInts.readUnsignedLong(buffer));
        movePlayerPacket.setPosition(new Vector3(
                buffer.readFloatLE(),
                buffer.readFloatLE(),
                buffer.readFloatLE()
        ));
        movePlayerPacket.setRotation(new Vector3(
                buffer.readFloatLE(),
                buffer.readFloatLE(),
                buffer.readFloatLE()
        ));
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

}
