package io.github.willqi.pizzaserver.api.event.type.server;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.event.Event;
import io.github.willqi.pizzaserver.api.event.type.CancellableType;

public abstract class BaseServerEvent extends Event {

    private final Server server;

    public BaseServerEvent(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }


    public static abstract class Cancellable extends BaseServerEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(Server server) {
            super(server);
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
