package io.github.pizzaserver.api.event.type.entity;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.event.Event;
import io.github.pizzaserver.api.event.type.CancellableType;

public abstract class BaseEntityEvent extends Event {

    protected Entity entity;


    public BaseEntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }


    public abstract static class Cancellable extends BaseEntityEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(Entity entity) {
            super(entity);
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
