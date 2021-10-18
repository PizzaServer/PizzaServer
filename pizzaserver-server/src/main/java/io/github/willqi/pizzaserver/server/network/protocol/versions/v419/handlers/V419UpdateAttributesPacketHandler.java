package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.entity.attributes.Attribute;
import io.github.willqi.pizzaserver.api.network.protocol.packets.UpdateAttributesPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.api.entity.attributes.AttributeType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class V419UpdateAttributesPacketHandler extends BaseProtocolPacketHandler<UpdateAttributesPacket> {

    @Override
    public void encode(UpdateAttributesPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getRuntimeEntityId());

        buffer.writeUnsignedVarInt(packet.getAttributes().size());
        for (Attribute attribute : packet.getAttributes()) {
            buffer.writeFloatLE(attribute.getMinimumValue());
            buffer.writeFloatLE(attribute.getMaximumValue());
            buffer.writeFloatLE(attribute.getCurrentValue());
            buffer.writeFloatLE(attribute.getDefaultValue());
            buffer.writeString(attribute.getType().getId());
        }
        buffer.writeUnsignedVarLong(packet.getTick());
    }

}
