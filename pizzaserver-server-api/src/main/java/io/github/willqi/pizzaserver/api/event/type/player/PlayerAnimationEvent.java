package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.player.data.AnimationAction;

/**
 * Called when a player triggers a player animation
 */
public class PlayerAnimationEvent extends BasePlayerEvent.Cancellable {

    protected final AnimationAction action;


    public PlayerAnimationEvent(Player player, AnimationAction action) {
        super(player);
        this.action = action;
    }

    public AnimationAction getAction() {
        return this.action;
    }

}
