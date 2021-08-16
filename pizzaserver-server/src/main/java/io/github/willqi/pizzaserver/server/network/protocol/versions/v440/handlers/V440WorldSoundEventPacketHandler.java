package io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers;

import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431WorldSoundEventPacketHandler;

public class V440WorldSoundEventPacketHandler extends V431WorldSoundEventPacketHandler {

    public V440WorldSoundEventPacketHandler() {
        this.sounds.put(WorldSound.COPPER_WAX_ON, 339);
        this.sounds.put(WorldSound.COPPER_WAX_OFF, 340);
        this.sounds.put(WorldSound.SCRAPE, 341);
        this.sounds.put(WorldSound.PLAYER_HURT_DROWN, 342);
        this.sounds.put(WorldSound.PLAYER_HURT_ON_FIRE, 343);
        this.sounds.put(WorldSound.PLAYER_HURT_FREEZE, 344);
        this.sounds.put(WorldSound.USE_SPYGLASS, 345);
        this.sounds.put(WorldSound.STOP_USING_SPYGLASS, 346);
        this.sounds.put(WorldSound.AMETHYST_BLOCK_CHIME, 347);
        this.sounds.put(WorldSound.AMBIENT_SCREAMER, 348);
        this.sounds.put(WorldSound.HURT_SCREAMER, 349);
        this.sounds.put(WorldSound.DEATH_SCREAMER, 350);
        this.sounds.put(WorldSound.MILK_SCREAMER, 351);
        this.sounds.put(WorldSound.JUMP_TO_BLOCK, 352);
        this.sounds.put(WorldSound.PRE_RAM, 353);
        this.sounds.put(WorldSound.PRE_RAM_SCREAMER, 354);
        this.sounds.put(WorldSound.RAM_IMPACT, 355);
        this.sounds.put(WorldSound.RAM_IMPACT_SCREAMER, 356);
        this.sounds.put(WorldSound.SQUID_INK_SQUIRT, 357);
        this.sounds.put(WorldSound.GLOW_SQUID_INK_SQUIRT, 358);
        this.sounds.put(WorldSound.CONVERT_TO_STRAY, 359);
    }

}
