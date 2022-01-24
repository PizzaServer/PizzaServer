package io.github.pizzaserver.api.event.type.block;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.IgniteCause;
import io.github.pizzaserver.api.entity.Entity;

public class BlockIgniteEvent extends BaseBlockEvent.Cancellable {

    protected final IgniteCause cause;
    protected final Entity entity;

    public BlockIgniteEvent(Block block, IgniteCause cause) {
        super(block);
        this.cause = cause;
        this.entity = null;
    }

    public BlockIgniteEvent(Block block, IgniteCause cause, Entity entity) {
        super(block);
        this.cause = cause;
        this.entity = entity;
    }

    public IgniteCause getCause() {
        return this.cause;
    }

    public Entity getEntity() {
        return this.entity;
    }

}
