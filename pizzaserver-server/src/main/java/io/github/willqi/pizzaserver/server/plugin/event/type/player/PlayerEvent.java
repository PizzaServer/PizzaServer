package io.github.willqi.pizzaserver.server.plugin.event.type.player;

import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.plugin.event.BaseEvent;
import io.github.willqi.pizzaserver.api.plugin.event.type.CancellableType;

public abstract class PlayerEvent extends BaseEvent {

    protected APIPlayer player;

    public PlayerEvent(APIPlayer player) {
        this.player = player;
    }

    public APIPlayer getPlayer() {
        return this.player;
    }


    public static abstract class Cancellable extends PlayerEvent implements CancellableType {

        protected boolean isCancelled;

        public Cancellable(APIPlayer player) {
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
