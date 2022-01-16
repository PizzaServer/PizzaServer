package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

/**
 * Called when a player starts/stops sneaking.
 */
public class PlayerToggleSneakingEvent extends BasePlayerEvent.Cancellable {

    protected boolean sneaking;


    public PlayerToggleSneakingEvent(Player player, boolean sneaking) {
        super(player);
        this.sneaking = sneaking;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }
}
