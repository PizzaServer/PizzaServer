package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.server.item.Item;

/**
 * Represents a potential item that can drop when a block is broken
 */
public class ImplBlockLoot {

    private final Item item;
    private final float probability;


    public ImplBlockLoot(Item item, float probability) {
        this.item = item;
        this.probability = probability;
    }

    public Item getItem() {
        return this.item;
    }

    /**
     * Chance for this loot to drop
     * @return
     */
    public float getProbability() {
        return this.probability;
    }

}
