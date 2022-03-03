package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

/**
 * Called when a player starts/stops sprinting.
 */
public class PlayerToggleSprintingEvent extends BasePlayerEvent.Cancellable {

    protected boolean sprinting;


    public PlayerToggleSprintingEvent(Player player, boolean sprinting) {
        super(player);
        this.sprinting = sprinting;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

}
