package io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.data.WorldEventType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419WorldEventPacketHandler;

public class V428WorldEventPacketHandler extends V419WorldEventPacketHandler {

    public V428WorldEventPacketHandler() {
        // particles
        this.events.put(WorldEventType.PARTICLE_VIBRATION_SIGNAL, 2027);

        // world
        this.events.put(WorldEventType.EVENT_CAULDRON_FILL_SNOW, 3514);
        this.events.put(WorldEventType.EVENT_CAULDRON_TAKE_SNOW, 3515);
    }

}
