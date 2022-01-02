package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.event.Event;
import io.github.pizzaserver.api.event.type.CancellableType;
import io.github.pizzaserver.api.player.Player;

public abstract class BasePlayerEvent extends Event {

    protected Player player;

    public BasePlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }


    public static abstract class Cancellable extends BasePlayerEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(Player player) {
            super(player);
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
