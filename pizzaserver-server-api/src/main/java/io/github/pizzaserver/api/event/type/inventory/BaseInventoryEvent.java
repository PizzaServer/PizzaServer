package io.github.pizzaserver.api.event.type.inventory;

import io.github.pizzaserver.api.inventory.Inventory;
import io.github.pizzaserver.api.event.Event;
import io.github.pizzaserver.api.event.type.CancellableType;

public class BaseInventoryEvent extends Event {

    protected Inventory inventory;

    public BaseInventoryEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return this.inventory;
    }


    public abstract static class Cancellable extends BaseInventoryEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(Inventory inventory) {
            super(inventory);
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
