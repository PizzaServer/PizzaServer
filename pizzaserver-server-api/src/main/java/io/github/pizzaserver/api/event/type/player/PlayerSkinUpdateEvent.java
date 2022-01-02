package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Skin;

/**
 * Called when the player changes their skin in-game.
 */
public class PlayerSkinUpdateEvent extends BasePlayerEvent.Cancellable {

    protected final Skin oldSkin;
    protected Skin newSkin;

    public PlayerSkinUpdateEvent(Player player, Skin newSkin) {
        super(player);
        this.oldSkin = player.getSkin();
        this.newSkin = newSkin;
    }

    public Skin getOldSkin() {
        return this.oldSkin;
    }

    public Skin getNewSkin() {
        return this.newSkin;
    }

    public void setNewSkin(Skin skin) {
        this.newSkin = skin;
    }

}
