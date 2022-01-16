package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when the player interacts with a block in the world.
 */
public class PlayerInteractEvent extends BasePlayerEvent.Cancellable {

    protected Block block;
    protected BlockFace blockFace;

    public PlayerInteractEvent(Player player, Block block, BlockFace blockFace) {
        super(player);
        this.block = block;
        this.blockFace = blockFace;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }
}
