package io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.WorldEventType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428WorldEventPacketHandler;

public class V431WorldEventPacketHandler extends V428WorldEventPacketHandler {

    public V431WorldEventPacketHandler() {
        this.events.put(WorldEventType.SOUND_POINTED_DRIPSTONE_LAND, 1064);
        this.events.put(WorldEventType.SOUND_DYE_USE, 1065);
        this.events.put(WorldEventType.SOUND_INK_SACK_USE, 1066);

        this.events.put(WorldEventType.PARTICLE_DRIPSTONE_DRIP, 2028);
        this.events.put(WorldEventType.PARTICLE_FIZZ_EFFECT, 2029);
        this.events.put(WorldEventType.PARTICLE_WAX_ON, 2030);
        this.events.put(WorldEventType.PARTICLE_WAX_OFF, 2031);
        this.events.put(WorldEventType.PARTICLE_SCRAPE, 2032);
        this.events.put(WorldEventType.PARTICLE_ELECTRIC_SPARK, 2033);

        this.events.put(WorldEventType.LEGACY_PARTICLE_STALACTITE_DRIP_WATER, 0x4000 + 29);
        this.events.put(WorldEventType.LEGACY_PARTICLE_STALACTITE_DRIP_LAVA, 0x4000 + 30);
        this.events.put(WorldEventType.LEGACY_PARTICLE_FALLING_DUST, 0x4000 + 31);
        this.events.put(WorldEventType.LEGACY_PARTICLE_MOB_SPELL, 0x4000 + 32);
        this.events.put(WorldEventType.LEGACY_PARTICLE_MOB_SPELL_AMBIENT, 0x4000 + 33);
        this.events.put(WorldEventType.LEGACY_PARTICLE_MOB_SPELL_INSTANTANEOUS, 0x4000 + 34);
        this.events.put(WorldEventType.LEGACY_PARTICLE_INK, 0x4000 + 35);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SLIME, 0x4000 + 36);
        this.events.put(WorldEventType.LEGACY_PARTICLE_RAIN_SPLASH, 0x4000 + 37);
        this.events.put(WorldEventType.LEGACY_PARTICLE_VILLAGER_ANGRY, 0x4000 + 38);
        this.events.put(WorldEventType.LEGACY_PARTICLE_VILLAGER_HAPPY, 0x4000 + 39);
        this.events.put(WorldEventType.LEGACY_PARTICLE_ENCHANTMENT_TABLE, 0x4000 + 40);
        this.events.put(WorldEventType.LEGACY_PARTICLE_TRACKING_EMITTER, 0x4000 + 41);
        this.events.put(WorldEventType.LEGACY_PARTICLE_NOTE, 0x4000 + 42);
        this.events.put(WorldEventType.LEGACY_PARTICLE_WITCH_SPELL, 0x4000 + 43);
        this.events.put(WorldEventType.LEGACY_PARTICLE_CARROT, 0x4000 + 44);
        this.events.put(WorldEventType.LEGACY_PARTICLE_MOB_APPEARANCE, 0x4000 + 45);
        this.events.put(WorldEventType.LEGACY_PARTICLE_END_ROD, 0x4000 + 46);
        this.events.put(WorldEventType.LEGACY_PARTICLE_DRAGONS_BREATH, 0x4000 + 47);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SPIT, 0x4000 + 48);
        this.events.put(WorldEventType.LEGACY_PARTICLE_TOTEM, 0x4000 + 49);
        this.events.put(WorldEventType.LEGACY_PARTICLE_FOOD, 0x4000 + 50);
        this.events.put(WorldEventType.LEGACY_PARTICLE_FIREWORKS_STARTER, 0x4000 + 51);
        this.events.put(WorldEventType.LEGACY_PARTICLE_FIREWORKS_SPARK, 0x4000 + 52);
        this.events.put(WorldEventType.LEGACY_PARTICLE_FIREWORKS_OVERLAY, 0x4000 + 53);
        this.events.put(WorldEventType.LEGACY_PARTICLE_BALLOON_GAS, 0x4000 + 54);
        this.events.put(WorldEventType.LEGACY_PARTICLE_COLOURED_FLAME, 0x4000 + 55);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SPARKLER, 0x4000 + 56);
        this.events.put(WorldEventType.LEGACY_PARTICLE_CONDUIT, 0x4000 + 57);
        this.events.put(WorldEventType.LEGACY_PARTICLE_BUBBLE_UP, 0x4000 + 58);
        this.events.put(WorldEventType.LEGACY_PARTICLE_BUBBLE_DOWN, 0x4000 + 59);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SNEEZE, 0x4000 + 60);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SHULKER_BULLET, 0x4000 + 61);
        this.events.put(WorldEventType.LEGACY_PARTICLE_BLEACH, 0x4000 + 62);
        this.events.put(WorldEventType.LEGACY_PARTICLE_DRAGON_DESTROY_BLOCK, 0x4000 + 63);
        this.events.put(WorldEventType.LEGACY_PARTICLE_MYCELIUM_DUST, 0x4000 + 64);
        this.events.put(WorldEventType.LEGACY_PARTICLE_FALLING_RED_DUST, 0x4000 + 65);
        this.events.put(WorldEventType.LEGACY_PARTICLE_CAMPFIRE_SMOKE, 0x4000 + 66);
        this.events.put(WorldEventType.LEGACY_PARTICLE_TALL_CAMPFIRE_SMOKE, 0x4000 + 67);
        this.events.put(WorldEventType.LEGACY_PARTICLE_RISING_DRAGONS_BREATH, 0x4000 + 68);
        this.events.put(WorldEventType.LEGACY_PARTICLE_FALLING_DRAGONS_BREATH, 0x4000 + 69);
        this.events.put(WorldEventType.LEGACY_PARTICLE_BLUE_FLAME, 0x4000 + 70);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SOUL, 0x4000 + 71);
        this.events.put(WorldEventType.LEGACY_PARTICLE_OBSIDIAN_TEAR, 0x4000 + 72);

    }

}
