package io.github.pizzaserver.api.event.type.block;

import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when the player places a block down.
 */
public class BlockPlaceEvent extends BaseBlockEvent.Cancellable {

    protected Player player;
    protected Block blockPlacedAgainst;

    public BlockPlaceEvent(Player player, Block block, Block blockPlacedAgainst) {
        super(block);
        this.player = player;
        this.blockPlacedAgainst = blockPlacedAgainst;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getBlockPlacedAgainst() {
        return this.blockPlacedAgainst;
    }

}
