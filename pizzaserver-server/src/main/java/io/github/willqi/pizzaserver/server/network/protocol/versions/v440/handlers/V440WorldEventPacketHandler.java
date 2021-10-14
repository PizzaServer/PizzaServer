package io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.WorldEventType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers.V431WorldEventPacketHandler;

public class V440WorldEventPacketHandler extends V431WorldEventPacketHandler {

    public V440WorldEventPacketHandler() {
        this.events.put(WorldEventType.LEGACY_PARTICLE_PORTAL_REVERSE, 0x4000 + 73);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SNOWFLAKE, 0x4000 + 74);
        this.events.put(WorldEventType.LEGACY_PARTICLE_VIBRATION_SIGNAL, 0x4000 + 75);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SCULK_SENSOR_REDSTONE, 0x4000 + 76);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SPORE_BLOSSOM_SHOWER, 0x4000 + 77);
        this.events.put(WorldEventType.LEGACY_PARTICLE_SPORE_BLOSSOM_AMBIENT, 0x40000 + 78);
        this.events.put(WorldEventType.LEGACY_PARTICLE_WAX, 0x4000 + 79);
        this.events.put(WorldEventType.LEGACY_PARTICLE_ELECTRIC_SPARK, 0x4000 + 80);
    }

}
