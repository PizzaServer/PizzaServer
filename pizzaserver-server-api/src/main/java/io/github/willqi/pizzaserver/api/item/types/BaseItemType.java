package io.github.willqi.pizzaserver.api.item.types;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;

import java.util.Collections;
import java.util.Set;

public abstract class BaseItemType {

    public abstract String getItemId();

    public abstract String getName();

    /**
     * The name of the icon provided in the minecraft:icon component
     * @return
     */
    public abstract String getIconName();

    public int getMaxStackSize() {
        return 64;
    }

    /**
     * Returns if this item can be placed in the offhand slot of a Player's inventory
     * @return if it can be placed in the offhand slot
     */
    public boolean isAllowedInOffHand() {
        return false;
    }

    /**
     * Returns if this item should be held visually like a tool
     * @return if the item should be held visually like a tool
     */
    public boolean isHandEquipped() {
        return true;    // TODO: Should this be false by default?
    }

    /**
     * Returns if this item should appear enchanted
     * @return if the item should appear enchanted
     */
    public boolean hasFoil() {
        return false;
    }

    /**
     * Get the only blocks that this item can break
     * If empty, this item is allowed to mine any block
     * @return only blocks this item can break
     */
    public Set<BaseBlockType> getOnlyBlocksCanBreak() {
        return Collections.emptySet();
    }

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