package io.github.pizzaserver.api.event.type.block;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.entity.Entity;

/**
 * Called when the player places a block down.
 */
public class BlockPlaceEvent extends BaseBlockEvent.Cancellable {

    protected Entity entity;
    protected Block blockPlacedAgainst;

    public BlockPlaceEvent(Entity entity, Block block, Block blockPlacedAgainst) {
        super(block);
        this.entity = entity;
        this.blockPlacedAgainst = blockPlacedAgainst;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Block getBlockPlacedAgainst() {
        return this.blockPlacedAgainst;
    }

}
