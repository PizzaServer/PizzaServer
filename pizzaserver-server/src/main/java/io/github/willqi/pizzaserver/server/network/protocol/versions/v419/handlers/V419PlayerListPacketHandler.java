package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerListPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419PlayerListPacketHandler extends BaseProtocolPacketHandler<PlayerListPacket> {

    @Override
    public void encode(PlayerListPacket packet, BasePacketBuffer buffer) {
        buffer.writeByte(packet.getActionType().ordinal()); // If more options are added, this will have to be modified
                                                            // to throw an error.
        buffer.writeUnsignedVarInt(packet.getEntries().size());
        switch (packet.getActionType()) {
            case ADD:
                for (PlayerList.Entry entry : packet.getEntries()) {
                    buffer.writeUUID(entry.getUUID());
                    buffer.writeVarLong(entry.getEntityRuntimeId());
                    buffer.writeString(entry.getUsername());
                    buffer.writeString(entry.getXUID());
                    buffer.writeString(entry.getPlatformChatId());
                    buffer.writeIntLE(entry.getDevice().getDeviceOS());
                    buffer.writeSkin(entry.getSkin());
                    buffer.writeBoolean(entry.isTeacher());
                    buffer.writeBoolean(entry.isHost());
                }
                for (PlayerList.Entry entry : packet.getEntries()) {
                    buffer.writeBoolean(entry.getSkin().isTrusted());
                }
                break;
            case REMOVE:
                for (PlayerList.Entry entry : packet.getEntries()) {
                    buffer.writeUUID(entry.getUUID());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unable to serialize PlayerListPacket due to unknown action type: " + packet.getActionType());
        }
    }

}
