package io.github.willqi.pizzaserver.api.item.types;

import io.github.willqi.pizzaserver.api.item.ItemStack;

public abstract class BaseItemType {

    public abstract String getItemId();

    public abstract String getName();

    /**
     * The name of the icon provided in the minecraft:icon component
     * @return
     */
    public abstract String getIconName();

    /**
     * Create an {@link ItemStack} of this item type
     * @return {@link ItemStack}
     */
    public ItemStack create() {
        return this.create(1);
    }

    /**
     * Create an {@link ItemStack} of this item type
     * @param amount amount of items in this item stack
     * @return {@link ItemStack}
     */
    public ItemStack create(int amount) {
        return this.create(1, 0);
    }

    /**
     * Create an {@link ItemStack} of this item type
     * @param amount amount of items in this item stack
     * @param damage damage of the item stack
     * @return {@link ItemStack}
     */
    public ItemStack create(int amount, int damage) {
        return new ItemStack(this, amount, damage);
    }

}