package io.github.willqi.pizzaserver.server.event;

import io.github.willqi.pizzaserver.server.event.type.CancellableType;

/**
 * A base event designed to be extended so that its
 * subclasses can be broadcast whenever an action occurs.
 */
public abstract class BaseEvent {

    // Does anything actually need to be here?
    // It's a class and not an interface because events should
    // Just be their own thing.

    public static abstract class Cancellable extends BaseEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable() {
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
