package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.player.attributes.Attribute;
import io.github.willqi.pizzaserver.api.network.protocol.packets.UpdateAttributesPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class V419UpdateAttributesPacketHandler extends BaseProtocolPacketHandler<UpdateAttributesPacket> {

    protected final Map<AttributeType, String> attributeIds = new HashMap<AttributeType, String>() {
        {
            this.put(AttributeType.HEALTH, "minecraft:health");
            this.put(AttributeType.ABSORPTION, "minecraft:absorption");
            this.put(AttributeType.FOOD, "minecraft:player.hunger");
            this.put(AttributeType.SATURATION, "minecraft:player.saturation");
            this.put(AttributeType.EXPERIENCE, "minecraft:player.experience");
            this.put(AttributeType.EXPERIENCE_LEVEL, "minecraft:player.level");
            this.put(AttributeType.MOVEMENT_SPEED, "minecraft:movement");
        }
    };

    @Override
    public void encode(UpdateAttributesPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getRuntimeEntityId());

        Set<Attribute> validAttributes = packet.getAttributes()
                .stream().filter(attribute -> this.attributeIds.containsKey(attribute.getType()))
                .collect(Collectors.toSet());

        buffer.writeUnsignedVarInt(validAttributes.size());
        for (Attribute attribute : validAttributes) {
            buffer.writeFloatLE(attribute.getMinimumValue());
            buffer.writeFloatLE(attribute.getMaximumValue());
            buffer.writeFloatLE(attribute.getCurrentValue());
            buffer.writeFloatLE(attribute.getDefaultValue());
            buffer.writeString(this.attributeIds.get(attribute.getType()));
        }
        buffer.writeUnsignedVarLong(packet.getTick());
    }

}
