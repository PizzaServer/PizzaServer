package io.github.pizzaserver.api.blockentity;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

public abstract class BaseBlockEntity implements BlockEntity {

    protected final Block block;

    public BaseBlockEntity(Block block) {
        this.block = block;
    }

    @Override
    public BlockLocation getLocation() {
        return this.block.getLocation();
    }

    @Override
    public int getLayer() {
        return this.block.getLayer();
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean onInteract(Player player) {
        return true;
    }

    @Override
    public void onPlace(Player player) {

    }

    @Override
    public void onBreak(Player player) {

    }

}
