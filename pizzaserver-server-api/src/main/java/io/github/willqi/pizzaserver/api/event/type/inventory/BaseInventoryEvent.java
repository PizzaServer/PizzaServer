package io.github.willqi.pizzaserver.api.event.type.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.event.Event;
import io.github.willqi.pizzaserver.api.event.type.CancellableType;

public class BaseInventoryEvent extends Event {

    protected Inventory inventory;

    public BaseInventoryEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return this.inventory;
    }


    public static abstract class Cancellable extends BaseInventoryEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(Inventory inventory) {
            super(inventory);
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
