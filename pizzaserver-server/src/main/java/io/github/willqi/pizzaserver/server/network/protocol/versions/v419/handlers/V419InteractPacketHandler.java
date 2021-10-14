package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.InteractPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419InteractPacketHandler extends BaseProtocolPacketHandler<InteractPacket> {

    @Override
    public InteractPacket decode(BasePacketBuffer buffer) {
        InteractPacket interactPacket = new InteractPacket();
        interactPacket.setAction(InteractPacket.Type.values()[buffer.readByte()]);
        interactPacket.setTargetEntityRuntimeId(buffer.readUnsignedVarLong());
        if (interactPacket.getAction() == InteractPacket.Type.MOUSE_OVER || interactPacket.getAction() == InteractPacket.Type.LEAVE_VEHICLE) {
            interactPacket.setPosition(buffer.readVector3());
        }
        return interactPacket;
    }
}
