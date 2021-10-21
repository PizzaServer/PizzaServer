package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.data.WorldEventType;
import io.github.willqi.pizzaserver.server.network.protocol.exceptions.OutdatedProtocolException;
import io.github.willqi.pizzaserver.api.network.protocol.packets.WorldEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.HashMap;
import java.util.Map;

public class V419WorldEventPacketHandler extends BaseProtocolPacketHandler<WorldEventPacket> {

    protected final Map<WorldEventType, Integer> events = new HashMap<WorldEventType, Integer>() {
        {
            // Sound
            this.put(WorldEventType.SOUND_CLICK, 1000);
            this.put(WorldEventType.SOUND_CLICK_FAILURE, 1001);
            this.put(WorldEventType.SOUND_SHOOT, 1002);
            this.put(WorldEventType.SOUND_DOOR, 1003);
            this.put(WorldEventType.SOUND_FIZZ, 1004);
            this.put(WorldEventType.SOUND_IGNITE, 1005);
            this.put(WorldEventType.SOUND_PLAY_RECORDING, 1006);
            this.put(WorldEventType.SOUND_GHAST, 1007);
            this.put(WorldEventType.SOUND_GHAST_SHOOT, 1008);
            this.put(WorldEventType.SOUND_BLAZE_SHOOT, 1009);
            this.put(WorldEventType.SOUND_DOOR_BUMP, 1010);

            this.put(WorldEventType.SOUND_DOOR_CRASH, 1012);

            this.put(WorldEventType.SOUND_ZOMBIE_INFECTED, 1016);
            this.put(WorldEventType.SOUND_ZOMBIE_CONVERTED, 1017);
            this.put(WorldEventType.SOUND_ENDERMAN_TELEPORT, 1018);

            this.put(WorldEventType.SOUND_ANVIL_BREAK, 1020);
            this.put(WorldEventType.SOUND_ANVIL_USE, 1021);
            this.put(WorldEventType.SOUND_ANVIL_FALL, 1022);

            this.put(WorldEventType.SOUND_ARROW_PICKUP, 1030);

            this.put(WorldEventType.SOUND_ENDER_PEARL, 1032);

            this.put(WorldEventType.SOUND_ITEM_FRAME_ADD_ITEM, 1040);
            this.put(WorldEventType.SOUND_ITEM_FRAME_BREAK, 1041);
            this.put(WorldEventType.SOUND_ITEM_FRAME_PLACE, 1042);
            this.put(WorldEventType.SOUND_ITEM_FRAME_REMOVE_ITEM, 1043);
            this.put(WorldEventType.SOUND_ITEM_FRAME_ROTATE_ITEM, 1044);
            this.put(WorldEventType.SOUND_CAMERA, 1050);
            this.put(WorldEventType.SOUND_ORB, 1051);
            this.put(WorldEventType.SOUND_TOTEM, 1052);

            this.put(WorldEventType.SOUND_ARMOUR_STAND_BREAK, 1060);
            this.put(WorldEventType.SOUND_ARMOUR_STAND_HIT, 1062);
            this.put(WorldEventType.SOUND_ARMOUR_STAND_FALL, 1063);
            this.put(WorldEventType.SOUND_ARMOUR_STAND_PLACE, 1064);

            // Particles
            this.put(WorldEventType.PARTICLE_SHOOT, 2000);
            this.put(WorldEventType.PARTICLE_DESTROY, 2001);
            this.put(WorldEventType.PARTICLE_SPLASH, 2002);
            this.put(WorldEventType.PARTICLE_EYE_DESPAWN, 2003);
            this.put(WorldEventType.PARTICLE_SPAWN, 2004);
            this.put(WorldEventType.PARTICLE_CROP_GROWTH, 2005);
            this.put(WorldEventType.PARTICLE_SOUND_GUARDIAN_CURSE, 2006);
            this.put(WorldEventType.PARTICLE_DEATH_SMOKE, 2007);
            this.put(WorldEventType.PARTICLE_BLOCK_FORCEFIELD, 2008);
            this.put(WorldEventType.PARTICLE_GENERIC_SPAWN, 2009);
            this.put(WorldEventType.PARTICLE_DRAGON_EGG_TELEPORT, 2010);
            this.put(WorldEventType.PARTICLE_CROP_EATEN, 2011);
            this.put(WorldEventType.PARTICLE_CRITICAL, 2012);
            this.put(WorldEventType.PARTICLE_ENDERMAN_TELEPORT, 2013);
            this.put(WorldEventType.PARTICLE_PUNCH_BLOCK, 2014);
            this.put(WorldEventType.PARTICLE_BUBBLE, 2015);
            this.put(WorldEventType.PARTICLE_EVAPORATE, 2016);
            this.put(WorldEventType.PARTICLE_DESTROY_ARMOR_STAND, 2017);
            this.put(WorldEventType.PARTICLE_BREAKING_EGG, 2018);
            this.put(WorldEventType.PARTICLE_DESTROY_EGG, 2019);
            this.put(WorldEventType.PARTICLE_EVAPORATE_WATER, 2020);
            this.put(WorldEventType.PARTICLE_DESTROY_BLOCK_SILENT, 2021);
            this.put(WorldEventType.PARTICLE_KNOCKBACK_ROAR, 2022);
            this.put(WorldEventType.PARTICLE_TELEPORT_TRAIL, 2023);
            this.put(WorldEventType.PARTICLE_POINT_CLOUD, 2024);
            this.put(WorldEventType.PARTICLE_EXPLOSION, 2025);
            this.put(WorldEventType.PARTICLE_BLOCK_EXPLOSION, 2026);

            // World
            this.put(WorldEventType.EVENT_START_RAIN, 3001);
            this.put(WorldEventType.EVENT_START_THUNDER, 3002);
            this.put(WorldEventType.EVENT_STOP_RAIN, 3003);
            this.put(WorldEventType.EVENT_STOP_THUNDER, 3004);
            this.put(WorldEventType.EVENT_PAUSE_GAME, 3005);
            this.put(WorldEventType.EVENT_SIMULATION_TIME_STEP, 3006);
            this.put(WorldEventType.EVENT_SIMULATION_TIME_SCALE, 3007);

            // Blocks
            this.put(WorldEventType.EVENT_REDSTONE_TRIGGER, 3500);
            this.put(WorldEventType.EVENT_CAULDRON_EXPLODE, 3501);
            this.put(WorldEventType.EVENT_CAULDRON_DYE_ARMOUR, 3502);
            this.put(WorldEventType.EVENT_CAULDRON_CLEAN_ARMOUR, 3503);
            this.put(WorldEventType.EVENT_CAULDRON_FILL_POTION, 3504);
            this.put(WorldEventType.EVENT_CAULDRON_TAKE_POTION, 3505);
            this.put(WorldEventType.EVENT_CAULDRON_FILL_WATER, 3506);
            this.put(WorldEventType.EVENT_CAULDRON_TAKE_WATER, 3507);
            this.put(WorldEventType.EVENT_CAULDRON_ADD_DYE, 3508);
            this.put(WorldEventType.EVENT_CAULDRON_CLEAN_BANNER, 3509);
            this.put(WorldEventType.EVENT_CAULDRON_FLUSH, 3510);
            this.put(WorldEventType.EVENT_AGENT_SPAWN_EFFECT, 3511);
            this.put(WorldEventType.EVENT_CAULDRON_FILL_LAVA, 3512);
            this.put(WorldEventType.EVENT_CAULDRON_TAKE_LAVA, 3513);

            this.put(WorldEventType.EVENT_BLOCK_START_BREAK, 3600);
            this.put(WorldEventType.EVENT_BLOCK_STOP_BREAK, 3601);
            this.put(WorldEventType.EVENT_BLOCK_UPDATE_BREAK, 3602);

            // Misc
            this.put(WorldEventType.EVENT_SET_DATA, 4000);

            this.put(WorldEventType.EVENT_PLAYERS_SLEEPING, 9800);
            this.put(WorldEventType.EVENT_JUMP_PREVENTED, 9810);

            // Legacy particles
            this.put(WorldEventType.LEGACY_PARTICLE_BUBBLES, 0x4000 + 1);
            this.put(WorldEventType.LEGACY_PARTICLE_BUBBLES_MANUAL, 0x4000 + 2);
            this.put(WorldEventType.LEGACY_PARTICLE_CRITICAL, 0x4000 + 3);
            this.put(WorldEventType.LEGACY_PARTICLE_BLOCK_FORCEFIELD, 0x4000 + 4);
            this.put(WorldEventType.LEGACY_PARTICLE_SMOKE, 0x4000 + 5);
            this.put(WorldEventType.LEGACY_PARTICLE_EXPLODE, 0x4000 + 6);
            this.put(WorldEventType.LEGACY_PARTICLE_EVAPORATION, 0x4000 + 7);
            this.put(WorldEventType.LEGACY_PARTICLE_FLAME, 0x4000 + 8);
            this.put(WorldEventType.LEGACY_PARTICLE_LAVA, 0x4000 + 9);
            this.put(WorldEventType.LEGACY_PARTICLE_LARGE_SMOKE, 0x4000 + 10);
            this.put(WorldEventType.LEGACY_PARTICLE_REDSTONE, 0x4000 + 11);
            this.put(WorldEventType.LEGACY_PARTICLE_RISING_RED_DUST, 0x4000 + 12);
            this.put(WorldEventType.LEGACY_PARTICLE_ITEM_BREAK, 0x4000 + 13);
            this.put(WorldEventType.LEGACY_PARTICLE_SNOWBALL_EXPLOSION, 0x4000 + 14);
            this.put(WorldEventType.LEGACY_PARTICLE_HUGE_EXPLOSION, 0x4000 + 15);
            this.put(WorldEventType.LEGACY_PARTICLE_HUGE_EXPLOSION_SEED, 0x4000 + 16);
            this.put(WorldEventType.LEGACY_PARTICLE_MOB_FLAME, 0x4000 + 17);
            this.put(WorldEventType.LEGACY_PARTICLE_HEART, 0x4000 + 18);
            this.put(WorldEventType.LEGACY_PARTICLE_TERRAIN, 0x4000 + 19);
            this.put(WorldEventType.LEGACY_PARTICLE_TOWN_AURA, 0x4000 + 20);
            this.put(WorldEventType.LEGACY_PARTICLE_PORTAL, 0x4000 + 21);
            this.put(WorldEventType.LEGACY_PARTICLE_MOB_PORTAL, 0x4000 + 22);
            this.put(WorldEventType.LEGACY_PARTICLE_SPLASH, 0x4000 + 23);
            this.put(WorldEventType.LEGACY_PARTICLE_SPLASH_MANUAL, 0x4000 + 24);
            this.put(WorldEventType.LEGACY_PARTICLE_WATER_WAKE, 0x4000 + 25);
            this.put(WorldEventType.LEGACY_PARTICLE_DRIP_WATER, 0x4000 + 26);
            this.put(WorldEventType.LEGACY_PARTICLE_DRIP_LAVA, 0x4000 + 27);
            this.put(WorldEventType.LEGACY_PARTICLE_DRIP_HONEY, 0x4000 + 28);
            this.put(WorldEventType.LEGACY_PARTICLE_FALLING_DUST, 0x4000 + 29);
            this.put(WorldEventType.LEGACY_PARTICLE_MOB_SPELL, 0x4000 + 30);
            this.put(WorldEventType.LEGACY_PARTICLE_MOB_SPELL_AMBIENT, 0x4000 + 31);
            this.put(WorldEventType.LEGACY_PARTICLE_MOB_SPELL_INSTANTANEOUS, 0x4000 + 32);
            this.put(WorldEventType.LEGACY_PARTICLE_INK, 0x4000 + 33);
            this.put(WorldEventType.LEGACY_PARTICLE_SLIME, 0x4000 + 34);
            this.put(WorldEventType.LEGACY_PARTICLE_RAIN_SPLASH, 0x4000 + 35);
            this.put(WorldEventType.LEGACY_PARTICLE_VILLAGER_ANGRY, 0x4000 + 36);
            this.put(WorldEventType.LEGACY_PARTICLE_VILLAGER_HAPPY, 0x4000 + 37);
            this.put(WorldEventType.LEGACY_PARTICLE_ENCHANTMENT_TABLE, 0x4000 + 38);
            this.put(WorldEventType.LEGACY_PARTICLE_TRACKING_EMITTER, 0x4000 + 39);
            this.put(WorldEventType.LEGACY_PARTICLE_NOTE, 0x4000 + 40);
            this.put(WorldEventType.LEGACY_PARTICLE_WITCH_SPELL, 0x4000 + 41);
            this.put(WorldEventType.LEGACY_PARTICLE_CARROT, 0x4000 + 42);
            this.put(WorldEventType.LEGACY_PARTICLE_MOB_APPEARANCE, 0x4000 + 43);
            this.put(WorldEventType.LEGACY_PARTICLE_END_ROD, 0x4000 + 44);
            this.put(WorldEventType.LEGACY_PARTICLE_DRAGONS_BREATH, 0x4000 + 45);
            this.put(WorldEventType.LEGACY_PARTICLE_SPIT, 0x4000 + 46);
            this.put(WorldEventType.LEGACY_PARTICLE_TOTEM, 0x4000 + 47);
            this.put(WorldEventType.LEGACY_PARTICLE_FOOD, 0x4000 + 48);
            this.put(WorldEventType.LEGACY_PARTICLE_FIREWORKS_STARTER, 0x4000 + 49);
            this.put(WorldEventType.LEGACY_PARTICLE_FIREWORKS_SPARK, 0x4000 + 50);
            this.put(WorldEventType.LEGACY_PARTICLE_FIREWORKS_OVERLAY, 0x4000 + 51);
            this.put(WorldEventType.LEGACY_PARTICLE_BALLOON_GAS, 0x4000 + 52);
            this.put(WorldEventType.LEGACY_PARTICLE_COLOURED_FLAME, 0x4000 + 53);
            this.put(WorldEventType.LEGACY_PARTICLE_SPARKLER, 0x4000 + 54);
            this.put(WorldEventType.LEGACY_PARTICLE_CONDUIT, 0x4000 + 55);
            this.put(WorldEventType.LEGACY_PARTICLE_BUBBLE_UP, 0x4000 + 56);
            this.put(WorldEventType.LEGACY_PARTICLE_BUBBLE_DOWN, 0x4000 + 57);
            this.put(WorldEventType.LEGACY_PARTICLE_SNEEZE, 0x4000 + 58);
            this.put(WorldEventType.LEGACY_PARTICLE_SHULKER_BULLET, 0x4000 + 59);
            this.put(WorldEventType.LEGACY_PARTICLE_BLEACH, 0x4000 + 60);
            this.put(WorldEventType.LEGACY_PARTICLE_DRAGON_DESTROY_BLOCK, 0x4000 + 61);
            this.put(WorldEventType.LEGACY_PARTICLE_MYCELIUM_DUST, 0x4000 + 62);
            this.put(WorldEventType.LEGACY_PARTICLE_FALLING_RED_DUST, 0x4000 + 63);
            this.put(WorldEventType.LEGACY_PARTICLE_CAMPFIRE_SMOKE, 0x4000 + 64);
            this.put(WorldEventType.LEGACY_PARTICLE_TALL_CAMPFIRE_SMOKE, 0x4000 + 65);
            this.put(WorldEventType.LEGACY_PARTICLE_RISING_DRAGONS_BREATH, 0x4000 + 66);
            this.put(WorldEventType.LEGACY_PARTICLE_FALLING_DRAGONS_BREATH, 0x4000 + 67);
            this.put(WorldEventType.LEGACY_PARTICLE_BLUE_FLAME, 0x4000 + 68);
            this.put(WorldEventType.LEGACY_PARTICLE_SOUL, 0x4000 + 69);
            this.put(WorldEventType.LEGACY_PARTICLE_OBSIDIAN_TEAR, 0x4000 + 70);
        }
    };

    @Override
    public void encode(WorldEventPacket packet, BasePacketBuffer buffer) {
        if (!this.events.containsKey(packet.getType())) {
            throw new OutdatedProtocolException(buffer.getVersion(), packet.getType() + " is not an implemented world event.");
        }

        buffer.writeVarInt(this.events.get(packet.getType()));
        buffer.writeVector3(packet.getPosition());
        buffer.writeVarInt(packet.getData());
    }

}
