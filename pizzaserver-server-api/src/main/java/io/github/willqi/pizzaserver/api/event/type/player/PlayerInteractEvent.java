package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockFace;
import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Called when the player interacts with a block in the world
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
