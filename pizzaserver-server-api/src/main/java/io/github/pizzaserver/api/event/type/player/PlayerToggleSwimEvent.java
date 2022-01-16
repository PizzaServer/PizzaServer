package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

/**
 * Called when the player begins swimming.
 */
public class PlayerToggleSwimEvent extends BasePlayerEvent.Cancellable {

    protected boolean swimming;


    public PlayerToggleSwimEvent(Player player, boolean swimming) {
        super(player);
        this.swimming = swimming;
    }

    public boolean isSwimming() {
        return this.swimming;
    }
}
