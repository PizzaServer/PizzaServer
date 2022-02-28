package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.block.impl.BlockBarrel;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

public class BlockEntityBarrel extends BlockEntityContainer {

    public static final String ID = "Barrel";

    public BlockEntityBarrel(BlockLocation blockLocation) {
        super(blockLocation);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

    @Override
    public void showOpenAnimation(Player player) {
        BlockBarrel barrel = (BlockBarrel) this.blockPosition.getBlock();
        if (!barrel.isOpen()) {
            barrel.setOpen(true);
            barrel.getWorld().setAndUpdateBlock(barrel, barrel.getLocation().toVector3i());
        }

        super.showOpenAnimation(player);
    }

    @Override
    public void showCloseAnimation(Player player) {
        BlockBarrel barrel = (BlockBarrel) this.blockPosition.getBlock();
        if (barrel.isOpen() && this.getInventory().getViewers().isEmpty()) {
            barrel.setOpen(false);
            barrel.getWorld().setAndUpdateBlock(barrel, barrel.getLocation().toVector3i());
        }

        super.showCloseAnimation(player);
    }

}
