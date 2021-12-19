package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.PlayerSkinPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419PlayerSkinPacketHandler extends BaseProtocolPacketHandler<PlayerSkinPacket> {

    @Override
    public PlayerSkinPacket decode(BasePacketBuffer buffer) {
        PlayerSkinPacket playerSkinPacket = new PlayerSkinPacket();
        playerSkinPacket.setPlayerUUID(buffer.readUUID());
        playerSkinPacket.setSkin(buffer.readSkin());
        playerSkinPacket.setNewSkinName(buffer.readString());
        playerSkinPacket.setOldSkinName(buffer.readString());
        playerSkinPacket.setTrusted(buffer.readBoolean());

        playerSkinPacket.getSkin().setTrusted(playerSkinPacket.isTrusted());

        return playerSkinPacket;
    }

    @Override
    public void encode(PlayerSkinPacket packet, BasePacketBuffer buffer) {
        buffer.writeUUID(packet.getPlayerUUID());
        buffer.writeSkin(packet.getSkin());
        buffer.writeString(packet.getNewSkinName());
        buffer.writeString(packet.getOldSkinName());
        buffer.writeBoolean(packet.getSkin().isTrusted());
    }

}
