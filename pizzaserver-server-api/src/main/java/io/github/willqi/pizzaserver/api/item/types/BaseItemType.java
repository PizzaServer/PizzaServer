package io.github.willqi.pizzaserver.api.item.types;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.player.Player;

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
        return false;    // TODO: Should this be false by default?
    }

    /**
     * If this item can be used on a liquid
     * @return if the item can be used on a liquid
     */
    public boolean canClickOnLiquids() {
        return false;
    }

    /**
     * Returns if this item should appear enchanted
     * @return if the item should appear enchanted
     */
    public boolean hasFoil() {
        return false;
    }

    /**
     * Returns if this item should appear flipped on the client
     * @return if this item should appear flipped on the client
     */
    public boolean isMirroredArt() {
        return false;
    }

    /**
     * Returns the animation to use when using this item
     * @return animation to use upon using this item
     */
    public UseAnimationType getUseAnimationType() {
        return UseAnimationType.NONE;
    }

    /**
     * Amount of ticks to show this animation for
     * @return how long to show the animation for
     */
    public int getUseDuration() {
        return 32;
    }

    /**
     * Returns if this item is separated by its damage
     * (e.g. durability)
     * @return if this item should be stacked by damage
     */
    public boolean isStackedByDamage() {
        return false;
    }

    /**
     * Returns the base amount of damage this item does
     * @return the base amount of damage this item does
     */
    public int getDamage() {
        return 1;
    }

    /**
     * Returns the level of mining speed this item provides
     * @return level of mining speed this item provides
     */
    public int getMiningSpeed() {
        return 1;
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
     * Called when the player interacts with a block using this item
     * @param player the player
     * @param itemStack the item stack
     * @param block the block interacted with
     */
    public void onInteract(Player player, ItemStack itemStack, Block block) {}

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
        return this.create(amount, 0);
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


    public enum UseAnimationType {
        NONE,
        FOOD,
        POTION
    }

}