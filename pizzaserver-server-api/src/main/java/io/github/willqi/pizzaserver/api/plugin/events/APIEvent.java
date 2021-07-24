package io.github.willqi.pizzaserver.api.plugin.events;

public abstract class APIEvent {

    private boolean cancelled;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        if (this instanceof Cancellable) {
            this.cancelled = cancelled;
        } else {
            throw new AssertionError(this.getClass().getName() + " cannot be cancelled.");
        }
    }

}
