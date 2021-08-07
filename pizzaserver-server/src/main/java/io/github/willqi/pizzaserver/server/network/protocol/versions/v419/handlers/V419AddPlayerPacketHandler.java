package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.data.EntityLink;
import io.github.willqi.pizzaserver.server.network.protocol.packets.AddPlayerPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419AddPlayerPacketHandler extends BaseProtocolPacketHandler<AddPlayerPacket> {

    @Override
    public void encode(AddPlayerPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeUUID(packet.getUUID(), buffer);
        helper.writeString(packet.getUsername(), buffer);
        VarInts.writeLong(buffer, packet.getEntityUniqueId());
        VarInts.writeUnsignedLong(buffer, packet.getEntityRuntimeId());
        helper.writeString(packet.getPlatformChatId(), buffer);

        helper.writeVector3(buffer, packet.getPosition());
        helper.writeVector3(buffer, packet.getVelocity());
        helper.writeVector3(buffer, new Vector3(packet.getPitch(), packet.getYaw(), packet.getHeadYaw()));
        buffer.writeByte(0);    // TODO: item serialization
        helper.writeEntityMetadata(packet.getMetaData(), buffer);

        // TODO: write proper adventure settings but here's a placeholder
        VarInts.writeUnsignedInt(buffer, 0);
        VarInts.writeUnsignedInt(buffer, 0);
        VarInts.writeUnsignedInt(buffer, 0);
        VarInts.writeUnsignedInt(buffer, 0);
        VarInts.writeUnsignedInt(buffer, 0);
        buffer.writeLongLE(packet.getEntityUniqueId());

        // write entity links
        VarInts.writeUnsignedInt(buffer, packet.getEntityLinks().size());
        for (EntityLink link : packet.getEntityLinks()) {
            helper.writeEntityLink(link, buffer);
        }
        helper.writeString(packet.getDeviceId(), buffer);
        buffer.writeIntLE(packet.getDevice().getDeviceOS());
    }

}
