package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.UpdateAttributesPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.player.attributes.Attribute;
import io.github.willqi.pizzaserver.server.player.attributes.AttributeType;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

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

        VarInts.writeUnsignedInt(buffer, packet.getAttributes().size());
        for (Attribute attribute : packet.getAttributes()) {
            if (!this.attributeIds.containsKey(attribute.getType())) {
                throw new UnsupportedOperationException("This version does not support attribute " + attribute.getType());
            }

            buffer.writeFloatLE(attribute.getMinimumValue());
            buffer.writeFloatLE(attribute.getMaximumValue());
            buffer.writeFloatLE(attribute.getCurrentValue());
            buffer.writeFloatLE(attribute.getDefaultValue());
            helper.writeString(this.attributeIds.get(attribute.getType()), buffer);
        }

        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

}
