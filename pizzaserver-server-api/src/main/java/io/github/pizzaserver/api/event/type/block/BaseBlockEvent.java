package io.github.pizzaserver.api.event.type.block;

import io.github.pizzaserver.api.event.Event;
import io.github.pizzaserver.api.event.type.CancellableType;
import io.github.pizzaserver.api.level.world.blocks.Block;

public abstract class BaseBlockEvent extends Event {

    protected Block block;

    public BaseBlockEvent(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }


    public abstract static class Cancellable extends BaseBlockEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(Block block) {
            super(block);
            this.isCancelled = false;
        }

        @Override
        public boolean isCancelled() {
            return this.isCancelled;
        }

        @Override
        public void setCancelled(boolean isCancelled) {
            this.isCancelled = isCancelled;
        }
    }

}
