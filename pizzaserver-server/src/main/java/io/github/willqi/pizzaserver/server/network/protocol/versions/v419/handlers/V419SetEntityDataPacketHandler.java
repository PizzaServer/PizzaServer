package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.*;
import java.util.stream.Collectors;

public class V419SetEntityDataPacketHandler extends BaseProtocolPacketHandler<SetEntityDataPacket> {

    protected final Map<EntityMetaFlagCategory, Integer> supportedFlagTypeIds = new HashMap<EntityMetaFlagCategory, Integer>(){
        {
            this.put(EntityMetaFlagCategory.DATA_FLAG, 0);
            this.put(EntityMetaFlagCategory.PLAYER_FLAG, 26);
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
            // Commented lines represent properties with unknown types
            this.put(EntityMetaPropertyName.HEALTH, 1);
            this.put(EntityMetaPropertyName.VARIANT, 2);
            this.put(EntityMetaPropertyName.COLOR, 3);
            this.put(EntityMetaPropertyName.NAMETAG, 4);
            this.put(EntityMetaPropertyName.OWNER_EID, 5);
            this.put(EntityMetaPropertyName.TARGET_EID, 6);
            this.put(EntityMetaPropertyName.AIR, 7);
            this.put(EntityMetaPropertyName.POTION_COLOR, 8);
            this.put(EntityMetaPropertyName.POTION_AMBIENT, 9);
            this.put(EntityMetaPropertyName.JUMP_DURATION, 10);
            this.put(EntityMetaPropertyName.HURT_TIME, 11);
            this.put(EntityMetaPropertyName.HURT_DIRECTION, 12);
            this.put(EntityMetaPropertyName.PADDLE_TIME_LEFT, 13);
            this.put(EntityMetaPropertyName.PADDLE_TIME_RIGHT, 14);
            this.put(EntityMetaPropertyName.EXPERIENCE_VALUE, 15);
            this.put(EntityMetaPropertyName.MINECART_DISPLAY_BLOCK, 16);
            this.put(EntityMetaPropertyName.MINECART_DISPLAY_OFFSET, 17);
            this.put(EntityMetaPropertyName.MINECART_HAS_DISPLAY, 18);
            // swell
            // old swell
            // swell dir
            this.put(EntityMetaPropertyName.CHARGE_AMOUNT, 22);
            this.put(EntityMetaPropertyName.ENDERMAN_HELD_ITEM_ID, 23);
            this.put(EntityMetaPropertyName.ENTITY_AGE, 24);
            // ???
            // player flags (used for flags)
            this.put(EntityMetaPropertyName.PLAYER_INDEX, 27);
            this.put(EntityMetaPropertyName.PLAYER_BED_POSITION, 28);
            this.put(EntityMetaPropertyName.FIREBALL_POWER_X, 29);
            this.put(EntityMetaPropertyName.FIREBALL_POWER_Y, 30);
            this.put(EntityMetaPropertyName.FIREBALL_POWER_Z, 31);
            // aux power
            // fish x
            // fish z
            // fish angle
            this.put(EntityMetaPropertyName.POTION_AUX_VALUE, 36);
            this.put(EntityMetaPropertyName.LEAD_HOLDER_EID, 37);
            this.put(EntityMetaPropertyName.SCALE, 38);
            this.put(EntityMetaPropertyName.INTERACTIVE_TAG, 39);
            this.put(EntityMetaPropertyName.NPC_SKIN_INDEX, 40);
            // url tag
            this.put(EntityMetaPropertyName.MAX_AIR_SUPPLY, 42);
            this.put(EntityMetaPropertyName.MARK_VARIANT, 43);
            this.put(EntityMetaPropertyName.CONTAINER_TYPE, 44);
            this.put(EntityMetaPropertyName.CONTAINER_BASE_SIZE, 45);
            this.put(EntityMetaPropertyName.CONTAINER_EXTRA_SLOTS_PER_STRENGTH, 46);
            this.put(EntityMetaPropertyName.BLOCK_TARGET, 47);
            this.put(EntityMetaPropertyName.WITHER_INVULNERABLE_TICKS, 48);
            this.put(EntityMetaPropertyName.WITHER_TARGET_1, 49);
            this.put(EntityMetaPropertyName.WITHER_TARGET_2, 50);
            this.put(EntityMetaPropertyName.WITHER_TARGET_3, 51);
            this.put(EntityMetaPropertyName.WITHER_AERIAL_ATTACK, 52);
            this.put(EntityMetaPropertyName.BOUNDING_BOX_WIDTH, 53);
            this.put(EntityMetaPropertyName.BOUNDING_BOX_HEIGHT, 54);
            this.put(EntityMetaPropertyName.FUSE_LENGTH, 55);
            this.put(EntityMetaPropertyName.RIDER_SEAT_POSITION, 56);
            this.put(EntityMetaPropertyName.RIDER_ROTATION_LOCKED, 57);
            this.put(EntityMetaPropertyName.RIDER_MAX_ROTATION, 58);
            this.put(EntityMetaPropertyName.RIDER_MIN_ROTATION, 59);
            this.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_RADIUS, 60);
            this.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_WAITING, 61);
            this.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_PARTICLE_ID, 62);
            this.put(EntityMetaPropertyName.SHULKER_ATTACH_FACE, 64);
            this.put(EntityMetaPropertyName.SHULKER_ATTACH_POSITION, 66);
            this.put(EntityMetaPropertyName.TRADING_TARGET_EID, 67);
            // trading career
            this.put(EntityMetaPropertyName.COMMAND_BLOCK_ENABLED, 69);
            this.put(EntityMetaPropertyName.COMMAND_BLOCK_COMMAND, 70);
            this.put(EntityMetaPropertyName.COMMAND_BLOCK_LAST_OUTPUT, 71);
            this.put(EntityMetaPropertyName.COMMAND_BLOCK_TRACK_OUTPUT, 72);
            this.put(EntityMetaPropertyName.CONTROLLING_RIDER_SEAT_NUMBER, 73);
            this.put(EntityMetaPropertyName.STRENGTH, 74);
            this.put(EntityMetaPropertyName.MAX_STRENGTH, 75);
            this.put(EntityMetaPropertyName.EVOKER_SPELL_COLOR, 76);
            this.put(EntityMetaPropertyName.LIMITED_LIFE, 77);
            this.put(EntityMetaPropertyName.ARMOR_STAND_POSE_INDEX, 78);
            this.put(EntityMetaPropertyName.ENDER_CRYSTAL_TIME_OFFSET, 79);
            this.put(EntityMetaPropertyName.ALWAYS_SHOW_NAMETAG, 80);
            this.put(EntityMetaPropertyName.COLOR_2, 81);
            // name author
            this.put(EntityMetaPropertyName.SCORE_TAG, 83);
            this.put(EntityMetaPropertyName.BALLOON_ATTACHED_ENTITY, 84);
            this.put(EntityMetaPropertyName.PUFFERFISH_SIZE, 85);
            this.put(EntityMetaPropertyName.BOAT_BUBBLE_TIME, 86);
            this.put(EntityMetaPropertyName.PLAYER_AGENT_EID, 87);
            // sitting amount
            // sitting amount previous
            this.put(EntityMetaPropertyName.EATING_COUNTER, 90);
            // flags extended (probably used for other flags?)
            // laying amount
            // laying amount previous
            this.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_DURATION, 94);
            this.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_SPAWN_TIME, 95);
            this.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_RATE, 96);
            this.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_ON_PICKUP, 97);
            // pickup count
            // interact text
            this.put(EntityMetaPropertyName.TRADE_TIER, 100);
            this.put(EntityMetaPropertyName.MAX_TRADE_TIER, 101);
            this.put(EntityMetaPropertyName.TRADE_XP, 102);
            this.put(EntityMetaPropertyName.SKIN_ID, 103);
            // spawning frames
            this.put(EntityMetaPropertyName.COMMAND_BLOCK_TICK_DELAY, 105);
            this.put(EntityMetaPropertyName.COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK, 106);
            this.put(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL, 107);
            this.put(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL_RANGE, 108);
            this.put(EntityMetaPropertyName.AMBIENT_SOUND_EVENT_NAME, 109);
            this.put(EntityMetaPropertyName.FALL_DAMAGE_MULTIPLIER, 110);
            // name raw text
            this.put(EntityMetaPropertyName.CAN_RIDE_TARGET, 112);
            this.put(EntityMetaPropertyName.LOW_TIER_CURED_TRADE_DISCOUNT, 113);
            this.put(EntityMetaPropertyName.HIGH_TIER_CURED_TRADE_DISCOUNT, 114);
            this.put(EntityMetaPropertyName.NEARBY_CURED_TRADE_DISCOUNT, 115);
            this.put(EntityMetaPropertyName.DISCOUNT_TIME_STAMP, 116);
            this.put(EntityMetaPropertyName.HITBOX, 117);
            this.put(EntityMetaPropertyName.IS_BUOYANT, 118);
            this.put(EntityMetaPropertyName.BUOYANCY_DATA, 119);
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
    public void encode(SetEntityDataPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getRuntimeId());

        // We need to write the metadata
        EntityMetaData metaData = packet.getData();

        // Filter for the flags we support
        Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> flags = metaData.getFlags();
        for (EntityMetaFlagCategory flagType : flags.keySet()) {
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
        buffer.writeUnsignedVarInt(totalEntries);

        for (EntityMetaFlagCategory flagType : flags.keySet()) {
            buffer.writeUnsignedVarInt(this.supportedFlagTypeIds.get(flagType));
            if (flagType == EntityMetaFlagCategory.PLAYER_FLAG) {
                byte flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1 << this.supportedFlags.get(flag);
                }
                buffer.writeUnsignedVarInt(propertyTypeIds.get(EntityMetaPropertyType.BYTE));
                buffer.writeByte(flagValue);
            } else {
                long flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1L << this.supportedFlags.get(flag);
                }
                buffer.writeUnsignedVarInt(propertyTypeIds.get(EntityMetaPropertyType.LONG));
                buffer.writeVarLong(flagValue);
            }
        }

        for (EntityMetaPropertyName propertyName : properties.keySet()) {
            buffer.writeUnsignedVarInt(this.supportedProperties.get(propertyName));
            buffer.writeUnsignedVarInt(this.propertyTypeIds.get(propertyName.getType()));
            switch (propertyName.getType()) {
                case BYTE:
                    buffer.writeByte((Byte)properties.get(propertyName).getValue());
                    break;
                case SHORT:
                    buffer.writeShortLE((Short)properties.get(propertyName).getValue());
                    break;
                case INTEGER:
                    buffer.writeVarInt((Integer)properties.get(propertyName).getValue());
                    break;
                case FLOAT:
                    buffer.writeFloatLE((Float)properties.get(propertyName).getValue());
                    break;
                case LONG:
                    buffer.writeVarLong((Long)properties.get(propertyName).getValue());
                    break;
                case STRING:
                    buffer.writeString((String)properties.get(propertyName).getValue());
                    break;
                case NBT:
                    buffer.writeNBTCompound((NBTCompound)properties.get(propertyName).getValue());
                    break;
                case VECTOR3I:
                    Vector3i vector3i = (Vector3i)properties.get(propertyName).getValue();
                    buffer.writeVector3i(vector3i);
                    break;
                case VECTOR3:
                    Vector3 vector3 = (Vector3)properties.get(propertyName).getValue();
                    buffer.writeVector3(vector3);
                    break;
                default:
                    throw new UnsupportedOperationException("Missing implementation when encoding entity meta type " + propertyName.getType());
            }
        }

        buffer.writeUnsignedVarLong(packet.getTick());
    }

}
