package io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419SetEntityDataPacketHandler;

public class V428SetEntityDataPacketHandler extends V419SetEntityDataPacketHandler {

    public V428SetEntityDataPacketHandler() {
        this.supportedFlags.put(EntityMetaFlag.IS_DOING_RAM_ATTACK, 96);

        this.supportedProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_RADIUS, 61);
        this.supportedProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_WAITING, 62);
        this.supportedProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_PARTICLE_ID, 63);
        this.supportedProperties.put(EntityMetaPropertyName.SHULKER_ATTACH_FACE, 65);
        this.supportedProperties.put(EntityMetaPropertyName.SHULKER_ATTACH_POSITION, 67);
        this.supportedProperties.put(EntityMetaPropertyName.TRADING_TARGET_EID, 68);
        // trading career
        this.supportedProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_ENABLED, 70);
        this.supportedProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_COMMAND, 71);
        this.supportedProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_LAST_OUTPUT, 72);
        this.supportedProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_TRACK_OUTPUT, 73);
        this.supportedProperties.put(EntityMetaPropertyName.CONTROLLING_RIDER_SEAT_NUMBER, 74);
        this.supportedProperties.put(EntityMetaPropertyName.STRENGTH, 75);
        this.supportedProperties.put(EntityMetaPropertyName.MAX_STRENGTH, 76);
        this.supportedProperties.put(EntityMetaPropertyName.EVOKER_SPELL_COLOR, 77);
        this.supportedProperties.put(EntityMetaPropertyName.LIMITED_LIFE, 78);
        this.supportedProperties.put(EntityMetaPropertyName.ARMOR_STAND_POSE_INDEX, 79);
        this.supportedProperties.put(EntityMetaPropertyName.ENDER_CRYSTAL_TIME_OFFSET, 80);
        this.supportedProperties.put(EntityMetaPropertyName.ALWAYS_SHOW_NAMETAG, 81);
        this.supportedProperties.put(EntityMetaPropertyName.COLOR_2, 82);
        // name author
        this.supportedProperties.put(EntityMetaPropertyName.SCORE_TAG, 84);
        this.supportedProperties.put(EntityMetaPropertyName.BALLOON_ATTACHED_ENTITY, 85);
        this.supportedProperties.put(EntityMetaPropertyName.PUFFERFISH_SIZE, 86);
        this.supportedProperties.put(EntityMetaPropertyName.BOAT_BUBBLE_TIME, 87);
        this.supportedProperties.put(EntityMetaPropertyName.PLAYER_AGENT_EID, 88);
        // sitting amount
        // sitting amount previous
        this.supportedProperties.put(EntityMetaPropertyName.EATING_COUNTER, 91);
        // flags extended (probably used for other flags?)
        // laying amount
        // laying amount previous
        this.supportedProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_DURATION, 95);
        this.supportedProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_SPAWN_TIME, 96);
        this.supportedProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_RATE, 97);
        this.supportedProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_ON_PICKUP, 98);
        // pickup count
        // interact text
        this.supportedProperties.put(EntityMetaPropertyName.TRADE_TIER, 101);
        this.supportedProperties.put(EntityMetaPropertyName.MAX_TRADE_TIER, 102);
        this.supportedProperties.put(EntityMetaPropertyName.TRADE_XP, 103);
        this.supportedProperties.put(EntityMetaPropertyName.SKIN_ID, 104);
        // spawning frames
        this.supportedProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_TICK_DELAY, 106);
        this.supportedProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK, 107);
        this.supportedProperties.put(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL, 108);
        this.supportedProperties.put(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL_RANGE, 109);
        this.supportedProperties.put(EntityMetaPropertyName.AMBIENT_SOUND_EVENT_NAME, 110);
        this.supportedProperties.put(EntityMetaPropertyName.FALL_DAMAGE_MULTIPLIER, 111);
        // name raw text
        this.supportedProperties.put(EntityMetaPropertyName.CAN_RIDE_TARGET, 113);
        this.supportedProperties.put(EntityMetaPropertyName.LOW_TIER_CURED_TRADE_DISCOUNT, 114);
        this.supportedProperties.put(EntityMetaPropertyName.HIGH_TIER_CURED_TRADE_DISCOUNT, 115);
        this.supportedProperties.put(EntityMetaPropertyName.NEARBY_CURED_TRADE_DISCOUNT, 116);
        this.supportedProperties.put(EntityMetaPropertyName.DISCOUNT_TIME_STAMP, 117);
        this.supportedProperties.put(EntityMetaPropertyName.HITBOX, 118);
        this.supportedProperties.put(EntityMetaPropertyName.IS_BUOYANT, 119);
        this.supportedProperties.put(EntityMetaPropertyName.BUOYANCY_DATA, 121);
    }

}
