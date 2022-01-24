package io.github.pizzaserver.api.block.behavior;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;

public interface BlockBehavior {

    /**
     * Called before an entity places a block.
     * Useful for entity dependent blockstates. (e.g. direction)
     * @param entity entity who placed the block
     * @param block the block being placed
     * @param face the face this block was placed on
     * @param clickPosition where this block was clicked
     * @return if the block should be placed
     */
    default boolean prepareForPlacement(Entity entity, Block block, BlockFace face, Vector3f clickPosition) {
        return true;
    }

    /**
     * Called after the block is placed in the world.
     * @param entity the entity who placed this block
     * @param block the block being placed
     * @param face the face this block was placed on
     */
    default void onPlace(Entity entity, Block block, BlockFace face) {}

    /**
     * Called when the right click button is used against this block.
     * @param player the player who interacted with the block
     * @param block the block interacted with
     * @param face block face that was clicked
     * @param clickPosition where the block was clicked
     * @return if the item used to interact with this block should be called
     */
    default boolean onInteract(Player player, Block block, BlockFace face, Vector3f clickPosition) {
        return true;
    }

    /**
     * Called whenever an entity breaks this block.
     * It is possible that no entity broke the block.
     * @param entity the entity that broke this block.
     * @param block the block broken
     */
    default void onBreak(Entity entity, Block block) {}

    /**
     * Called whenever an entity moves onto this block.
     * @param entity the entity walking on the block
     * @param block the block walked on
     */
    default void onWalkedOn(Entity entity, Block block) {}

    /**
     * Called whenever an entity walks off of this block.
     * @param entity the entity who walked off this block
     * @param block the block walked off of
     */
    default void onWalkedOff(Entity entity, Block block) {}

    /**
     * Called every tick an entity is on this block.
     * @param entity the entity who is on this block
     * @param block the block the entity is standing on
     */
    default void onStandingOn(Entity entity, Block block) {}

    /**
     * Called ever tick an entity is colliding with a block.
     * @param entity the entity who is colliding with the block
     * @param block the block they're colliding with
     */
    default void onCollision(Entity entity, Block block) {}

    /**
     * Called whenever the block is updated.
     * @param block the block being updated
     */
    default void onUpdate(BlockUpdateType type, Block block) {}

}
