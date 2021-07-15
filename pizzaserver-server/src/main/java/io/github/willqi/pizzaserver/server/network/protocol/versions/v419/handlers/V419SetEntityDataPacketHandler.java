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
            this.put(EntityMetaFlag.IS_ON_FIRE, 0);
            this.put(EntityMetaFlag.IS_SNEAKING, 1);
            this.put(EntityMetaFlag.IS_RIDING, 2);
            this.put(EntityMetaFlag.IS_SPRINTING, 3);
            this.put(EntityMetaFlag.IS_USING_ITEM, 4);
            this.put(EntityMetaFlag.IS_INVISIBLE, 5);
            this.put(EntityMetaFlag.IS_TEMPTED, 6);
            this.put(EntityMetaFlag.IS_IN_LOVE, 7);
            this.put(EntityMetaFlag.IS_SADDLED, 8);
            this.put(EntityMetaFlag.IS_POWERED, 9);
            this.put(EntityMetaFlag.IS_IGNITED, 10);
            this.put(EntityMetaFlag.IS_BABY, 11);
            this.put(EntityMetaFlag.IS_CONVERTING, 12);
            this.put(EntityMetaFlag.CRITICAL, 13);
            this.put(EntityMetaFlag.CAN_SHOW_NAMETAG, 14);
            this.put(EntityMetaFlag.ALWAYS_SHOW_NAMETAG, 15);
            this.put(EntityMetaFlag.HAS_NO_AI, 16);
            this.put(EntityMetaFlag.IS_SILENT, 17);
            this.put(EntityMetaFlag.IS_WALL_CLIMBING, 18);
            this.put(EntityMetaFlag.CAN_WALL_CLIMB, 19);
            this.put(EntityMetaFlag.CAN_SWIM, 20);
            this.put(EntityMetaFlag.CAN_FLY, 21);
            this.put(EntityMetaFlag.CAN_WALK, 22);
            this.put(EntityMetaFlag.IS_RESTING, 23);
            this.put(EntityMetaFlag.IS_SITTING, 24);
            this.put(EntityMetaFlag.IS_ANGRY, 25);
            this.put(EntityMetaFlag.IS_INTERESTED, 26);
            this.put(EntityMetaFlag.IS_CHARGED, 27);
            this.put(EntityMetaFlag.IS_TAMED, 28);
            this.put(EntityMetaFlag.IS_ORPHANED, 29);
            this.put(EntityMetaFlag.IS_LEASHED, 30);
            this.put(EntityMetaFlag.IS_SHEARED, 31);
            this.put(EntityMetaFlag.IS_GLIDING, 32);
            this.put(EntityMetaFlag.ELDER, 33);
            this.put(EntityMetaFlag.IS_MOVING, 34);
            this.put(EntityMetaFlag.IS_BREATHING, 35);
            this.put(EntityMetaFlag.CHESTED, 36);
            this.put(EntityMetaFlag.STACKABLE, 37);
            this.put(EntityMetaFlag.SHOW_BASE, 38);
            this.put(EntityMetaFlag.IS_STANDING, 39);
            this.put(EntityMetaFlag.IS_SHAKING, 40);
            this.put(EntityMetaFlag.IS_IDLING, 41);
            this.put(EntityMetaFlag.IS_CASTING, 42);
            this.put(EntityMetaFlag.IS_CHARGING, 43);
            this.put(EntityMetaFlag.IS_WASD_CONTROLLED, 44);
            this.put(EntityMetaFlag.CAN_POWER_JUMP, 45);
            this.put(EntityMetaFlag.LINGER, 46);
            this.put(EntityMetaFlag.HAS_COLLISION, 47);
            this.put(EntityMetaFlag.HAS_GRAVITY, 48);
            this.put(EntityMetaFlag.IS_FIRE_IMMUNE, 49);
            this.put(EntityMetaFlag.IS_DANCING, 50);
            this.put(EntityMetaFlag.ENCHANTED, 51);
            this.put(EntityMetaFlag.SHOWING_TRIDENT_ROPE, 52);
            this.put(EntityMetaFlag.HAS_PRIVATE_CONTAINER, 53);
            this.put(EntityMetaFlag.IS_TRANSFORMING, 54);
            this.put(EntityMetaFlag.SPIN_ATTACK, 55);
            this.put(EntityMetaFlag.IS_SWIMMING, 56);
            this.put(EntityMetaFlag.IS_BRIBED, 57);
            this.put(EntityMetaFlag.IS_PREGNANT, 58);
            this.put(EntityMetaFlag.IS_LAYING_EGG, 59);
            this.put(EntityMetaFlag.RIDER_CAN_PICK, 60);
            this.put(EntityMetaFlag.TRANSITION_SITTING, 61);
            this.put(EntityMetaFlag.IS_EATING, 62);
            this.put(EntityMetaFlag.IS_LAYING_DOWN, 63);
            this.put(EntityMetaFlag.IS_SNEEZING, 64);
            this.put(EntityMetaFlag.TRUSTING, 65);
            this.put(EntityMetaFlag.IS_ROLLING, 66);
            this.put(EntityMetaFlag.IS_SCARED, 67);
            this.put(EntityMetaFlag.IN_SCAFFOLDING, 68);
            this.put(EntityMetaFlag.OVER_SCAFFOLDING, 69);
            this.put(EntityMetaFlag.FALLING_THROUGH_SCAFFOLDING, 70);
            this.put(EntityMetaFlag.IS_BLOCKING, 71);
            this.put(EntityMetaFlag.TRANSITION_BLOCKING, 72);
            this.put(EntityMetaFlag.BLOCKED_USING_SHIELD, 73);
            this.put(EntityMetaFlag.BLOCKED_USING_DAMAGED_SHIELD, 74);
            this.put(EntityMetaFlag.IS_SLEEPING, 75);
            this.put(EntityMetaFlag.WANTS_TO_AWAKE, 76);
            this.put(EntityMetaFlag.HAS_TRADE_INTEREST, 77);
            this.put(EntityMetaFlag.IS_DOOR_BREAKER, 78);
            this.put(EntityMetaFlag.IS_BREAKING_OBSTRUCTION, 79);
            this.put(EntityMetaFlag.IS_DOOR_OPENER, 80);
            this.put(EntityMetaFlag.IS_ILLAGER_CAPTAIN, 81);
            this.put(EntityMetaFlag.IS_STUNNED, 82);
            this.put(EntityMetaFlag.IS_ROARING, 83);
            this.put(EntityMetaFlag.HAS_DELAYED_ATTACK, 84);
            this.put(EntityMetaFlag.IS_AVOIDING_MOBS, 85);
            this.put(EntityMetaFlag.IS_AVOIDING_BLOCK, 86);
            this.put(EntityMetaFlag.IS_FACING_TARGET_TO_RANGE_ATTACK, 87);
            this.put(EntityMetaFlag.IS_HIDDEN_WHEN_INVISIBLE, 88);
            this.put(EntityMetaFlag.IS_IN_UI, 89);
            this.put(EntityMetaFlag.IS_STALKING, 90);
            this.put(EntityMetaFlag.IS_EMOTING, 91);
            this.put(EntityMetaFlag.IS_CELEBRATING, 92);
            this.put(EntityMetaFlag.IS_ADMIRING, 93);
            this.put(EntityMetaFlag.IS_CELEBRATING_SPECIAL, 94);
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
