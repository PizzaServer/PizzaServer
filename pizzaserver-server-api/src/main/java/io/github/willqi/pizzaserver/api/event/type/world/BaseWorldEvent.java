package io.github.willqi.pizzaserver.api.event.type.world;

import io.github.willqi.pizzaserver.api.event.Event;
import io.github.willqi.pizzaserver.api.event.type.CancellableType;
import io.github.willqi.pizzaserver.api.world.World;

public class BaseWorldEvent extends Event {

    protected World world;

    public BaseWorldEvent(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }


    public static abstract class Cancellable extends BaseWorldEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(World world) {
            super(world);
            this.isCancelled = false;
        }

        @Override
        public boolean isCancelled() {
            return isCancelled;
        }

        @Override
        public void setCancelled(boolean isCancelled) {
            this.isCancelled = isCancelled;
        }
    }
}
