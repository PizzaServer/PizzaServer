package io.github.pizzaserver.server.blockentity.type;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Set;

public abstract class BaseBlockEntity<T extends Block> implements BlockEntity<T> {

    protected final BlockLocation blockLocation;
    protected boolean updated;


    public BaseBlockEntity(BlockLocation location) {
        this.blockLocation = location;
    }

    /**
     * Retrieve all block ids that are valid under this block entity.
     * If a block is set in this location without this block id, the block entity will be destroyed.
     * @return set of all block ids valid for this entity
     */
    public abstract Set<String> getBlockIds();

    public void tick() {

    }

    public void update() {
        this.updated = true;
    }

    /**
     * If this block entity was updated.
     * Querying this will set the update request to false.
     * @return if the block entity was updated
     */
    public boolean requestedUpdate() {
        if (this.updated) {
            this.updated = false;
            return true;
        }
        return false;
    }

    public boolean onInteract(Player player) {
        return true;
    }

    /**
     * Called after the block associated with this entity is placed.
     * @param player the player who placed the block
     */
    public void onPlace(Player player) {

    }

    /**
     * Called right before the block associated with this entity is broken.
     * @param player the player who broke the block
     */
    public void onBreak(Player player) {

    }

    @Override
    public BlockLocation getLocation() {
        return this.blockLocation;
    }

}
