package io.github.willqi.pizzaserver.api.entity.definition.components.impl;

import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;

import java.util.Optional;

/**
 * Entity component that creates a boss bar for this entity when in range.
 */
public class EntityBossComponent extends EntityComponent {

    private final String name;
    private final int range;
    private final boolean darkenSky;

    /**
     * Creates a boss bar when in distance to this entity.
     * @param name name of the boss bar. If none is present it will use the name of the entity
     * @param range amount of blocks a player must be to see this boss bar
     * @param darkenSky if the boss bar should darken the sky
     */
    public EntityBossComponent(String name, int range, boolean darkenSky) {
        this.name = name;
        this.range = range;
        this.darkenSky = darkenSky;
    }

    /**
     * Creates a boss bar when in distance to this entity.
     * @param range amount of blocks a player must be to see this boss bar
     * @param darkenSky if the boss bar should darken the sky
     */
    public EntityBossComponent(int range, boolean darkenSky) {
        this(null, range, darkenSky);
    }

    public Optional<String> getBossName() {
        return Optional.ofNullable(this.name);
    }

    public int getRange() {
        return this.range;
    }

    public boolean shouldDarkenSky() {
        return this.darkenSky;
    }

    @Override
    public String getName() {
        return "minecraft:boss";
    }

}
