package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerListPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419PlayerListPacketHandler extends BaseProtocolPacketHandler<PlayerListPacket> {

    @Override
    public void encode(PlayerListPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        buffer.writeByte(packet.getActionType().ordinal()); // If more options are added, this will have to be modified
                                                            // to throw an error.
        VarInts.writeUnsignedInt(buffer, packet.getEntries().size());
        switch (packet.getActionType()) {
            case ADD:
                for (PlayerListPacket.Entry entry : packet.getEntries()) {
                    helper.writeString(entry.getUUID().toString(), buffer);
                    VarInts.writeLong(buffer, entry.getEntityRuntimeId());
                    helper.writeString(entry.getUsername(), buffer);
                    helper.writeString(entry.getXUID(), buffer);
                    helper.writeString(entry.getPlatformChatId(), buffer);
                    buffer.writeIntLE(entry.getDevice().getDeviceOS());
                    helper.writeSkin(buffer, entry.getSkin());
                    buffer.writeBoolean(entry.isTeacher());
                    buffer.writeBoolean(entry.isHost());
                }
                for (PlayerListPacket.Entry entry : packet.getEntries()) {
                    buffer.writeBoolean(entry.getSkin().isTrusted());
                }
                break;
            case REMOVE:
                for (PlayerListPacket.Entry entry : packet.getEntries()) {
                    helper.writeString(entry.getUUID().toString(), buffer);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unable to serialize PlayerListPacket due to unknown action type: " + packet.getActionType());
        }
    }

}
