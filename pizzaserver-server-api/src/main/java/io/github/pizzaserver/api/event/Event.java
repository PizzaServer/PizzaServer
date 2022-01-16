package io.github.pizzaserver.api.event;

import io.github.pizzaserver.api.event.type.CancellableType;

public abstract class Event {

    // Does anything actually need to be here?
    // It's a class and not an interface because events should
    // Just be their own thing.


    public static abstract class Cancellable extends Event implements CancellableType {

        protected boolean isCancelled;

        public Cancellable() {
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
