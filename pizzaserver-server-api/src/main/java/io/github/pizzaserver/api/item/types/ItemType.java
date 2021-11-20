package io.github.pizzaserver.api.item.types;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.types.components.ArmorItemComponent;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.level.world.blocks.BlockFace;
import io.github.pizzaserver.api.player.Player;

import java.util.Set;

public interface ItemType {

    String getItemId();

    String getName();

    /**
     * The name of the icon provided in the minecraft:icon component.
     * @return minecraft:icon value
     */
    String getIconName();

    /**
     * The max stack size for this item type.
     * @return max stack size
     */
    int getMaxStackSize();

    /**
     * Returns if this item can be placed in the offhand slot of a Player's inventory.
     * @return if it can be placed in the offhand slot
     */
    boolean isAllowedInOffHand();

    /**
     * Returns if this item should be held visually like a tool.
     * @return if the item should be held visually like a tool
     */
    boolean isHandEquipped();

    /**
     * If this item can be used on a liquid.
     * @return if the item can be used on a liquid
     */
    boolean canClickOnLiquids();

    /**
     * Returns if this item should appear enchanted.
     * @return if the item should appear enchanted
     */
    boolean hasFoil();

    /**
     * Returns if this item should appear flipped on the client.
     * @return if this item should appear flipped on the client
     */
    boolean isMirroredArt();

    /**
     * Returns the animation to use when using this item.
     * @return animation to use upon using this item
     */
    ItemType.UseAnimationType getUseAnimationType();

    /**
     * Amount of ticks to show this animation for.
     * @return how long to show the animation for
     */
    int getUseDuration();

    /**
     * Returns if this item is separated by its damage.
     * (e.g. durability)
     * @return if this item should be stacked by damage
     */
    boolean isStackedByDamage();

    /**
     * Returns the base amount of damage this item does.
     * @return the base amount of damage this item does
     */
    int getDamage();

    /**
     * Get the tool type this item is considered as.
     * @return tool type
     */
    ToolType getToolType();

    /**
     * Returns the strength of the tool against a block.
     * @return level of mining speed this item provides against a block
     */
    float getToolStrength(Block block);

    /**
     * Get the only blocks that this item can break.
     * If empty, this item is allowed to mine any block
     * @return only blocks this item can break
     */
    Set<BaseBlockType> getOnlyBlocksCanBreak();

    /**
     * Called when the player interacts with a block using this item.
     * @param player the player
     * @param itemStack the item stack
     * @param block the block interacted with
     * @param blockFace the block face that was clicked
     *
     * @return if the interaction was successful. an incorrect interaction will resend the item slot and the blocks interacted with
     */
    boolean onInteract(Player player, ItemStack itemStack, Block block, BlockFace blockFace);

    /**
     * Called when the player interacts with an entity using this item.
     * @param player the player
     * @param itemStack the item stack
     * @param entity the entity interacted with
     */
    void onInteract(Player player, ItemStack itemStack, Entity entity);

    /**
     * Create an {@link ItemStack} of this item type.
     * @return {@link ItemStack}
     */
    ItemStack create();

    /**
     * Create an {@link ItemStack} of this item type.
     * @param amount amount of items in this item stack
     * @return {@link ItemStack}
     */
    ItemStack create(int amount);

    /**
     * Create an {@link ItemStack} of this item type.
     * @param amount amount of items in this item stack
     * @param damage damage of the item stack
     * @return {@link ItemStack}
     */
    ItemStack create(int amount, int damage);


    enum UseAnimationType {
        NONE,
        FOOD,
        POTION
    }

    static boolean canBePlacedInSlot(ItemType itemType, ContainerSlotType containerSlotType) {
        switch (containerSlotType) {
            case ARMOR:
                return itemType instanceof ArmorItemComponent;
            case OFFHAND:
                return itemType.isAllowedInOffHand();
            default:
                return true;
        }
    }

}
