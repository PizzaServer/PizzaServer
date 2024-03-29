package io.github.pizzaserver.api.event.type.world;

import io.github.pizzaserver.api.event.Event;
import io.github.pizzaserver.api.event.type.CancellableType;
import io.github.pizzaserver.api.level.world.World;

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
            return this.isCancelled;
        }

        @Override
        public void setCancelled(boolean isCancelled) {
            this.isCancelled = isCancelled;
        }
    }
}
