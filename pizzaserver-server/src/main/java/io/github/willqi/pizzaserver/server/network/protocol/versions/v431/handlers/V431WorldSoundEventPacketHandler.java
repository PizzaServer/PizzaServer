package io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers;

import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428WorldSoundEventPacketHandler;

public class V431WorldSoundEventPacketHandler extends V428WorldSoundEventPacketHandler {

    public V431WorldSoundEventPacketHandler() {
        this.sounds.put(WorldSound.POINTED_DRIPSTONE_CAULDRON_DRIP_LAVA, 332);
        this.sounds.put(WorldSound.POINTED_DRIPSTONE_CAULDRON_DRIP_WATER, 333);
        this.sounds.put(WorldSound.POINTED_DRIPSTONE_DRIP_LAVA, 334);
        this.sounds.put(WorldSound.POINTED_DRIPSTONE_DRIP_WATER, 335);
        this.sounds.put(WorldSound.CAVE_VINES_PICK_BERRIES, 336);
        this.sounds.put(WorldSound.BIG_DRIPLEAF_TILT_DOWN, 337);
        this.sounds.put(WorldSound.BIG_DRIPLEAF_TILT_UP, 338);
    }

}
