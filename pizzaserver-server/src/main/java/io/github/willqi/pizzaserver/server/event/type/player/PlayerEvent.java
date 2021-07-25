package io.github.willqi.pizzaserver.server.event.type.player;

import io.github.willqi.pizzaserver.server.event.BaseEvent;
import io.github.willqi.pizzaserver.server.event.type.CancellableType;
import io.github.willqi.pizzaserver.server.player.Player;

public abstract class PlayerEvent extends BaseEvent {

    protected Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }



    public static abstract class Cancellable extends PlayerEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(Player player) {
            super(player);
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
