package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

/**
 * Called when the player selects a new slot in their hotbar.
 */
public class PlayerHotbarSelectEvent extends BasePlayerEvent.Cancellable {

    protected int slot;

    public PlayerHotbarSelectEvent(Player player, int slot) {
        super(player);
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
