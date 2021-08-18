package io.github.willqi.pizzaserver.server.network.protocol.versions.v448.handlers;

import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431WorldSoundEventPacketHandler;

public class V448WorldSoundEventPacketHandler extends V431WorldSoundEventPacketHandler {

    public V448WorldSoundEventPacketHandler() {
        this.sounds.put(WorldSound.CAKE_ADD_CANDLE, 360);
        this.sounds.put(WorldSound.EXTINGUISH_CANDLE, 361);
        this.sounds.put(WorldSound.AMBIENT_CANDLE, 362);
    }

}
