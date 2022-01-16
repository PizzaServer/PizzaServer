package io.github.pizzaserver.api.event.type.block;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when a player stops breaking a block.
 */
public class BlockStopBreakEvent extends BaseBlockEvent {

    protected Player player;

    public BlockStopBreakEvent(Player player, Block block) {
        super(block);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}
