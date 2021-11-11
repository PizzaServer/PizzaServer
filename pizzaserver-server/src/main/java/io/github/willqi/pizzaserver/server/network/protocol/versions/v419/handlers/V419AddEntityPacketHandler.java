package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.entity.data.attributes.Attribute;
import io.github.willqi.pizzaserver.api.network.protocol.data.EntityLink;
import io.github.willqi.pizzaserver.api.network.protocol.packets.AddEntityPacket;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419AddEntityPacketHandler extends BaseProtocolPacketHandler<AddEntityPacket> {

    @Override
    public void encode(AddEntityPacket packet, BasePacketBuffer buffer) {
        buffer.writeVarLong(packet.getEntityUniqueId());
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        buffer.writeString(packet.getEntityType());
        buffer.writeVector3(packet.getPosition());
        buffer.writeVector3(packet.getVelocity());
        buffer.writeVector3(new Vector3(packet.getPitch(), packet.getYaw(), packet.getHeadYaw()));
        buffer.writeUnsignedVarInt(packet.getAttributes().size());
        for (Attribute attribute : packet.getAttributes()) {
            buffer.writeString(attribute.getType().toString());
            buffer.writeFloatLE(attribute.getMinimumValue());
            buffer.writeFloatLE(attribute.getMaximumValue());
            buffer.writeFloatLE(attribute.getCurrentValue());
        }
        buffer.writeEntityMetadata(packet.getMetaData());
        buffer.writeUnsignedVarInt(packet.getEntityLinks().size());
        for (EntityLink entityLink : packet.getEntityLinks()) {
            buffer.writeEntityLink(entityLink);
        }
    }

}
