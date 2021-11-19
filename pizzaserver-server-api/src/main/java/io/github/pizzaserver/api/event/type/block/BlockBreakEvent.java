package io.github.pizzaserver.api.event.type.block;

import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when a player breaks a block.
 */
public class BlockBreakEvent extends BaseBlockEvent.Cancellable {

    protected Player player;
    protected boolean doBlockDrops = true;

    public BlockBreakEvent(Player player, Block block) {
        super(block);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isBlockDropsEnabled() {
        return this.doBlockDrops;
    }

    public void setBlockDropsEnabled(boolean enabled) {
        this.doBlockDrops = enabled;
    }

}
