package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.AnimatePacket;

/**
 * Called when a player triggers a player animation.
 */
public class PlayerAnimationEvent extends BasePlayerEvent.Cancellable {

    protected final AnimatePacket.Action action;


    public PlayerAnimationEvent(Player player, AnimatePacket.Action action) {
        super(player);
        this.action = action;
    }

    public AnimatePacket.Action getAction() {
        return this.action;
    }

}
