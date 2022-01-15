package io.github.pizzaserver.api.entity.definition.components.impl;

import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.item.Item;

import java.util.List;

/**
 * Entity component that defines the loot dropped.
 */
public class EntityLootComponent extends EntityComponent {

    private final List<Item> loot;

    public EntityLootComponent(List<Item> loot) {
        this.loot = loot;
    }

    @Override
    public String getName() {
        return "minecraft:loot";
    }

    /**
     * Defines the loot dropped when killed.
     * @return loot dropped
     * @deprecated When loot tables are implemented, this should be replaced.
     */
    @Deprecated
    public List<Item> getLoot() {
        return this.loot;
    }

}
