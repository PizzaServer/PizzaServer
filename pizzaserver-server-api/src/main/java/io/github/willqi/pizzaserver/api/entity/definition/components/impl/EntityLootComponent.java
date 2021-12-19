package io.github.willqi.pizzaserver.api.entity.definition.components.impl;

import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.item.ItemStack;

import java.util.List;

/**
 * Entity component that defines the loot dropped.
 */
public class EntityLootComponent extends EntityComponent {

    private final List<ItemStack> loot;

    public EntityLootComponent(List<ItemStack> loot) {
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
    public List<ItemStack> getLoot() {
        return this.loot;
    }

}
