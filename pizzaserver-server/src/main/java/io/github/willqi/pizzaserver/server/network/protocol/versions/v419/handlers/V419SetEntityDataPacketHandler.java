package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlagType;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaPropertyType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.*;
import java.util.stream.Collectors;

public class V419SetEntityDataPacketHandler extends ProtocolPacketHandler<SetEntityDataPacket> {

    protected final Map<EntityMetaFlagType, Integer> supportedFlagTypeIds = new HashMap<EntityMetaFlagType, Integer>(){
        {
            this.put(EntityMetaFlagType.DATA_FLAG, 0);
            this.put(EntityMetaFlagType.PLAYER_FLAG, 26);
        }
    };

    protected final Map<EntityMetaFlag, Integer> supportedFlags = new HashMap<EntityMetaFlag, Integer>(){
        {
            this.put(EntityMetaFlag.ON_FIRE, 0);
            this.put(EntityMetaFlag.SNEAKING, 1);
            this.put(EntityMetaFlag.SPRINTING, 3);
            this.put(EntityMetaFlag.CAN_SHOW_NAMETAG, 14);
            this.put(EntityMetaFlag.ALWAYS_SHOW_NAMETAG, 15);
            this.put(EntityMetaFlag.NO_AI, 16);
        }
    };

    protected final Map<EntityMetaPropertyName, Integer> supportedProperties = new HashMap<EntityMetaPropertyName, Integer>(){
        {

        }
    };

    protected final Map<EntityMetaPropertyType, Integer> propertyTypeIds = new HashMap<EntityMetaPropertyType, Integer>(){
        {
            this.put(EntityMetaPropertyType.BYTE, 0);
            this.put(EntityMetaPropertyType.SHORT, 1);
            this.put(EntityMetaPropertyType.INTEGER, 2);
            this.put(EntityMetaPropertyType.FLOAT, 3);
            this.put(EntityMetaPropertyType.STRING, 4);
            this.put(EntityMetaPropertyType.NBT, 5);
            this.put(EntityMetaPropertyType.VECTOR3I, 6);
            this.put(EntityMetaPropertyType.LONG, 7);
            this.put(EntityMetaPropertyType.VECTOR3, 8);
        }
    };


    @Override
    public void encode(SetEntityDataPacket packet, ByteBuf buffer, PacketHelper helper) {

        VarInts.writeUnsignedLong(buffer, packet.getRuntimeId());

        // We need to write the metadata
        EntityMetaData metaData = packet.getData();

        // Filter for the flags we support
        Map<EntityMetaFlagType, Set<EntityMetaFlag>> flags = metaData.getFlags();
        for (EntityMetaFlagType flagType : flags.keySet()) {
            Set<EntityMetaFlag> supportedFlags = flags.get(flagType).stream()
                    .filter(this.supportedFlags::containsKey)
                    .collect(Collectors.toSet());

            flags.put(flagType, supportedFlags);
        }

        // Filter for the properties we support
        Map<EntityMetaPropertyName, EntityMetaProperty<?>> properties = new HashMap<>(metaData.getProperties());
        properties.keySet().removeIf(propertyName -> !this.supportedProperties.containsKey(propertyName));

        // Serialize all entries
        int totalEntries = flags.keySet().size() + properties.size();
        VarInts.writeUnsignedInt(buffer, totalEntries);

        for (EntityMetaFlagType flagType : flags.keySet()) {
            VarInts.writeUnsignedInt(buffer, this.supportedFlagTypeIds.get(flagType));
            if (flagType == EntityMetaFlagType.PLAYER_FLAG) {
                byte flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1 << this.supportedFlags.get(flag);
                }
                VarInts.writeUnsignedInt(buffer, propertyTypeIds.get(EntityMetaPropertyType.BYTE));
                buffer.writeByte(flagValue);
            } else {
                long flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1L << this.supportedFlags.get(flag);
                }
                VarInts.writeUnsignedInt(buffer, propertyTypeIds.get(EntityMetaPropertyType.LONG));
                VarInts.writeLong(buffer, flagValue);
            }
        }

        for (EntityMetaPropertyName propertyName : properties.keySet()) {
            VarInts.writeUnsignedInt(buffer, this.supportedProperties.get(propertyName));
            VarInts.writeUnsignedInt(buffer, this.propertyTypeIds.get(propertyName.getType()));
            switch (propertyName.getType()) {
                case BYTE:
                    buffer.writeByte(((EntityMetaProperty<Byte>)properties.get(propertyName)).getValue());
                    break;
                case SHORT:
                    buffer.writeShortLE(((EntityMetaProperty<Short>)properties.get(propertyName)).getValue());
                    break;
                case INTEGER:
                    VarInts.writeInt(buffer, ((EntityMetaProperty<Integer>)properties.get(propertyName)).getValue());
                    break;
                case FLOAT:
                    buffer.writeFloatLE(((EntityMetaProperty<Float>)properties.get(propertyName)).getValue());
                    break;
                case LONG:
                    VarInts.writeLong(buffer, ((EntityMetaProperty<Long>)properties.get(propertyName)).getValue());
                    break;
                case STRING:
                    helper.writeString(((EntityMetaProperty<String>)properties.get(propertyName)).getValue(), buffer);
                    break;
                case NBT:
                    helper.writeNBTCompound(((EntityMetaProperty<NBTCompound>)properties.get(propertyName)).getValue(), buffer);
                    break;
                case VECTOR3I:
                    Vector3i vector3i = ((EntityMetaProperty<Vector3i>)properties.get(propertyName)).getValue();
                    VarInts.writeInt(buffer, vector3i.getX());
                    VarInts.writeInt(buffer, vector3i.getY());
                    VarInts.writeInt(buffer, vector3i.getZ());
                    break;
                case VECTOR3:
                    Vector3 vector3 = ((EntityMetaProperty<Vector3>)properties.get(propertyName)).getValue();
                    buffer.writeFloatLE(vector3.getX());
                    buffer.writeFloatLE(vector3.getY());
                    buffer.writeFloatLE(vector3.getZ());
                    break;
                default:
                    throw new UnsupportedOperationException("Missing implementation when encoding entity meta type " + propertyName.getType());
            }
        }

        VarInts.writeUnsignedLong(buffer, packet.getTick());
    }

}
