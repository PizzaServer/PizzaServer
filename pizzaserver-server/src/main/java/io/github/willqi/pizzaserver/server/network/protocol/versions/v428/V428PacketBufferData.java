package io.github.willqi.pizzaserver.server.network.protocol.versions.v428;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.V422PacketBufferData;

public class V428PacketBufferData extends V422PacketBufferData {

    public static final BasePacketBufferData INSTANCE = new V428PacketBufferData();


    protected V428PacketBufferData() {
        this.registerEntityFlag(EntityMetaFlag.IS_DOING_RAM_ATTACK, 96);

        this.registerEntityProperty(EntityMetaPropertyName.AREA_EFFECT_CLOUD_RADIUS, 61)
            .registerEntityProperty(EntityMetaPropertyName.AREA_EFFECT_CLOUD_WAITING, 62)
            .registerEntityProperty(EntityMetaPropertyName.AREA_EFFECT_CLOUD_PARTICLE_ID, 63)
            .registerEntityProperty(EntityMetaPropertyName.SHULKER_ATTACH_FACE, 65)
            .registerEntityProperty(EntityMetaPropertyName.SHULKER_ATTACH_POSITION, 67)
            .registerEntityProperty(EntityMetaPropertyName.TRADING_TARGET_EID, 68)
            // trading career
            .registerEntityProperty(EntityMetaPropertyName.COMMAND_BLOCK_ENABLED, 70)
            .registerEntityProperty(EntityMetaPropertyName.COMMAND_BLOCK_COMMAND, 71)
            .registerEntityProperty(EntityMetaPropertyName.COMMAND_BLOCK_LAST_OUTPUT, 72)
            .registerEntityProperty(EntityMetaPropertyName.COMMAND_BLOCK_TRACK_OUTPUT, 73)
            .registerEntityProperty(EntityMetaPropertyName.CONTROLLING_RIDER_SEAT_NUMBER, 74)
            .registerEntityProperty(EntityMetaPropertyName.STRENGTH, 75)
            .registerEntityProperty(EntityMetaPropertyName.MAX_STRENGTH, 76)
            .registerEntityProperty(EntityMetaPropertyName.EVOKER_SPELL_COLOR, 77)
            .registerEntityProperty(EntityMetaPropertyName.LIMITED_LIFE, 78)
            .registerEntityProperty(EntityMetaPropertyName.ARMOR_STAND_POSE_INDEX, 79)
            .registerEntityProperty(EntityMetaPropertyName.ENDER_CRYSTAL_TIME_OFFSET, 80)
            .registerEntityProperty(EntityMetaPropertyName.ALWAYS_SHOW_NAMETAG, 81)
            .registerEntityProperty(EntityMetaPropertyName.COLOR_2, 82)
            // name author
            .registerEntityProperty(EntityMetaPropertyName.SCORE_TAG, 84)
            .registerEntityProperty(EntityMetaPropertyName.BALLOON_ATTACHED_ENTITY, 85)
            .registerEntityProperty(EntityMetaPropertyName.PUFFERFISH_SIZE, 86)
            .registerEntityProperty(EntityMetaPropertyName.BOAT_BUBBLE_TIME, 87)
            .registerEntityProperty(EntityMetaPropertyName.PLAYER_AGENT_EID, 88)
            // sitting amount
            // sitting amount previous
            .registerEntityProperty(EntityMetaPropertyName.EATING_COUNTER, 91)
            // flags extended (probably used for other flags?)
            // laying amount
            // laying amount previous
            .registerEntityProperty(EntityMetaPropertyName.AREA_EFFECT_CLOUD_DURATION, 95)
            .registerEntityProperty(EntityMetaPropertyName.AREA_EFFECT_CLOUD_SPAWN_TIME, 96)
            .registerEntityProperty(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_RATE, 97)
            .registerEntityProperty(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_ON_PICKUP, 98)
            // pickup count
            // interact text
            .registerEntityProperty(EntityMetaPropertyName.TRADE_TIER, 101)
            .registerEntityProperty(EntityMetaPropertyName.MAX_TRADE_TIER, 102)
            .registerEntityProperty(EntityMetaPropertyName.TRADE_XP, 103)
            .registerEntityProperty(EntityMetaPropertyName.SKIN_ID, 104)
            // spawning frames
            .registerEntityProperty(EntityMetaPropertyName.COMMAND_BLOCK_TICK_DELAY, 106)
            .registerEntityProperty(EntityMetaPropertyName.COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK, 107)
            .registerEntityProperty(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL, 108)
            .registerEntityProperty(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL_RANGE, 109)
            .registerEntityProperty(EntityMetaPropertyName.AMBIENT_SOUND_EVENT_NAME, 110)
            .registerEntityProperty(EntityMetaPropertyName.FALL_DAMAGE_MULTIPLIER, 111)
            // name raw text
            .registerEntityProperty(EntityMetaPropertyName.CAN_RIDE_TARGET, 113)
            .registerEntityProperty(EntityMetaPropertyName.LOW_TIER_CURED_TRADE_DISCOUNT, 114)
            .registerEntityProperty(EntityMetaPropertyName.HIGH_TIER_CURED_TRADE_DISCOUNT, 115)
            .registerEntityProperty(EntityMetaPropertyName.NEARBY_CURED_TRADE_DISCOUNT, 116)
            .registerEntityProperty(EntityMetaPropertyName.DISCOUNT_TIME_STAMP, 117)
            .registerEntityProperty(EntityMetaPropertyName.HITBOX, 118)
            .registerEntityProperty(EntityMetaPropertyName.IS_BUOYANT, 119)
            .registerEntityProperty(EntityMetaPropertyName.BUOYANCY_DATA, 121);
    }

}
