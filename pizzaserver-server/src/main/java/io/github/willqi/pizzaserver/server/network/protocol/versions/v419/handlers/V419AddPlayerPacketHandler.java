package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.api.network.protocol.data.EntityLink;
import io.github.willqi.pizzaserver.api.network.protocol.packets.AddPlayerPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419AddPlayerPacketHandler extends BaseProtocolPacketHandler<AddPlayerPacket> {

    @Override
    public void encode(AddPlayerPacket packet, BasePacketBuffer buffer) {
        buffer.writeUUID(packet.getUUID());
        buffer.writeString(packet.getUsername());
        buffer.writeVarLong(packet.getEntityUniqueId());
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        buffer.writeString(packet.getPlatformChatId());

        buffer.writeVector3(packet.getPosition());
        buffer.writeVector3(packet.getVelocity());
        buffer.writeVector3(new Vector3(packet.getPitch(), packet.getYaw(), packet.getHeadYaw()));
        buffer.writeItem(packet.getHeldItem());
        buffer.writeEntityMetadata(packet.getMetaData());

        // TODO: write proper adventure settings but here's a placeholder
        buffer.writeUnsignedVarInt(0);
        buffer.writeUnsignedVarInt(0);
        buffer.writeUnsignedVarInt(0);
        buffer.writeUnsignedVarInt(0);
        buffer.writeUnsignedVarInt(0);
        buffer.writeLongLE(packet.getEntityUniqueId());

        // write entity links
        buffer.writeUnsignedVarInt(packet.getEntityLinks().size());
        for (EntityLink link : packet.getEntityLinks()) {
            buffer.writeEntityLink(link);
        }
        buffer.writeString(packet.getDeviceId());
        buffer.writeIntLE(packet.getDevice().getDeviceOS());
    }

}
