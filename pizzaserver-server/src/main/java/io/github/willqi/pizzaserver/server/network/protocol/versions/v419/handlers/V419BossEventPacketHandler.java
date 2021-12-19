package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BossEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.exceptions.ProtocolException;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419BossEventPacketHandler extends BaseProtocolPacketHandler<BossEventPacket> {

    @Override
    public BossEventPacket decode(BasePacketBuffer buffer) {
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setEntityRuntimeId(buffer.readVarLong());

        BossEventPacket.Type type = BossEventPacket.Type.values()[buffer.readUnsignedVarInt()];
        bossEventPacket.setType(type);
        switch (type) {
            case REGISTER_PLAYER:
            case UNREGISTER_PLAYER:
                bossEventPacket.setPlayerRuntimeId(buffer.readVarLong());
                break;
            default:
                throw new ProtocolException(buffer.getVersion(), "Unsupported boss event type retrieved from client.");
        }
        return bossEventPacket;
    }

    @Override
    public void encode(BossEventPacket packet, BasePacketBuffer buffer) {
        buffer.writeVarLong(packet.getEntityRuntimeId());
        buffer.writeUnsignedVarInt(packet.getType().ordinal());
        switch (packet.getType()) {
            case SHOW:
                buffer.writeString(packet.getTitle());
                buffer.writeFloatLE(packet.getPercentage());
                buffer.writeShortLE(packet.getDarkenSkyValue());
                buffer.writeUnsignedVarInt(packet.getColour());
                buffer.writeUnsignedVarInt(packet.getOverlay());
                break;
            case CHANGE_HEALTH_PERCENTAGE:
                buffer.writeFloatLE(packet.getPercentage());
                break;
            case CHANGE_TITLE:
                buffer.writeString(packet.getTitle());
                break;
            case CHANGE_APPEARANCE:
                buffer.writeShortLE(packet.getDarkenSkyValue());
                buffer.writeUnsignedVarInt(packet.getColour());
                buffer.writeUnsignedVarInt(packet.getOverlay());
                break;
            case CHANGE_TEXTURE:
                buffer.writeUnsignedVarInt(packet.getColour());
                buffer.writeUnsignedVarInt(packet.getOverlay());
                break;
        }
    }

}
