package io.github.pizzaserver.api.item.behavior;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;

public interface ItemBehavior<T extends Item> {

    /**
     * Called when the player interacts with a block using this item.
     *
     * @param player the player
     * @param item the item
     * @param block the block interacted with
     * @param blockFace the block face that was clicked
     * @param clickPosition where the block was clicked
     * @return if the interaction was successful. an incorrect interaction will resend the item slot and the blocks interacted with
     */
    boolean onInteract(Player player, T item, Block block, BlockFace blockFace, Vector3f clickPosition);

    /**
     * Called when the player interacts with an entity using this item.
     *
     * @param player the player
     * @param item the item
     * @param entity the entity interacted with
     */
    void onInteract(Player player, T item, Entity entity);

    /**
     * Called when the player breaks a block with this item.
     *
     * @param player the player
     * @param item the item
     * @param block the block broken
     */
    void onBreak(Player player, T item, Block block);

}
