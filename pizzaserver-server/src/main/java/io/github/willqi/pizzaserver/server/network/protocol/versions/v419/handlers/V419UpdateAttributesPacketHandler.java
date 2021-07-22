package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.player.attributes.APIAttribute;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.UpdateAttributesPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class V419UpdateAttributesPacketHandler extends ProtocolPacketHandler<UpdateAttributesPacket> {

    protected final Map<AttributeType, String> attributeIds = new HashMap<AttributeType, String>(){
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
    public void encode(UpdateAttributesPacket packet, ByteBuf buffer, PacketHelper helper) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());

        Set<APIAttribute> validAttributes = packet.getAttributes()
                .stream().filter(attribute -> this.attributeIds.containsKey(attribute.getType()))
                .collect(Collectors.toSet());

        VarInts.writeUnsignedInt(buffer, validAttributes.size());
        for (APIAttribute attribute : validAttributes) {
            buffer.writeFloatLE(attribute.getMinimumValue());
            buffer.writeFloatLE(attribute.getMaximumValue());
            buffer.writeFloatLE(attribute.getCurrentValue());
            buffer.writeFloatLE(attribute.getDefaultValue());
            helper.writeString(this.attributeIds.get(attribute.getType()), buffer);
        }
        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

}
