package io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers;

import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419WorldSoundEventPacketHandler;

public class V428WorldSoundEventPacketHandler extends V419WorldSoundEventPacketHandler {

    public V428WorldSoundEventPacketHandler() {
        this.sounds.put(WorldSound.AMBIENT_LOOP_WARPED_FOREST, 318);
        this.sounds.put(WorldSound.AMBIENT_LOOP_SOULSAND_VALLEY, 319);
        this.sounds.put(WorldSound.AMBIENT_LOOP_NETHER_WASTES, 320);
        this.sounds.put(WorldSound.AMBIENT_LOOP_BASALT_DELTAS, 321);
        this.sounds.put(WorldSound.AMBIENT_LOOP_CRIMSON_FOREST, 322);
        this.sounds.put(WorldSound.AMBIENT_ADDITION_WARPED_FOREST, 323);
        this.sounds.put(WorldSound.AMBIENT_ADDITION_SOULSAND_VALLEY, 324);;
        this.sounds.put(WorldSound.AMBIENT_ADDITION_NETHER_WASTES, 325);
        this.sounds.put(WorldSound.AMBIENT_ADDITION_BASALT_DELTAS, 326);
        this.sounds.put(WorldSound.AMBIENT_ADDITION_CRIMSON_FOREST, 327);
        this.sounds.put(WorldSound.SCULK_SENSOR_POWER_ON, 328);
        this.sounds.put(WorldSound.SCULK_SENSOR_POWER_OFF, 329);
        this.sounds.put(WorldSound.BUCKET_FILL_POWDER_SNOW, 330);
        this.sounds.put(WorldSound.BUCKET_EMPTY_POWDER_SNOW, 331);
    }

}
