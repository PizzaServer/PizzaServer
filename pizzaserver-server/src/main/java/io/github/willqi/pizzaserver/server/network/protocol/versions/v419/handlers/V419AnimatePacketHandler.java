package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.AnimatePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419AnimatePacketHandler extends BaseProtocolPacketHandler<AnimatePacket> {

    @Override
    public AnimatePacket decode(ByteBuf buffer, BasePacketHelper helper) {
        AnimatePacket animatePacket = new AnimatePacket();
        animatePacket.setActionID(buffer.readInt());
        animatePacket.setEntityRuntimeID(buffer.readLong());
        return animatePacket;
    }

    @Override
    public void encode(AnimatePacket packet, ByteBuf buffer, BasePacketHelper helper) {
        buffer.writeInt(packet.getActionID());
        buffer.writeLong(packet.getEntityRuntimeID());
    }

    public enum Action {
        NO_ACTION(0),
        SWING_ARM(1),
        WAKE_UP(3),
        CRITICAL_HIT(4),
        MAGIC_CRITICAL_HIT(5),
        ROW_RIGHT(128),
        ROW_LEFT(129);

        public final int id;
        Action(int id) {
            this.id = id;
        }
    }

}
