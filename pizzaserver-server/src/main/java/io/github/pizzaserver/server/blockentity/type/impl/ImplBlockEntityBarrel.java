package io.github.pizzaserver.server.blockentity.type.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockBarrel;
import io.github.pizzaserver.api.blockentity.type.BlockEntityBarrel;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityBarrel extends ImplBlockEntityContainer<BlockBarrel> implements BlockEntityBarrel {

    public static final Set<String> BLOCK_IDS = Collections.singleton(BlockID.BARREL);

    public ImplBlockEntityBarrel(BlockLocation location) {
        super(location, ContainerType.CONTAINER);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

    @Override
    public void showOpenAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showOpenAnimation(player);
        }
    }

    @Override
    public void showOpenAnimation(Player player) {
        BlockBarrel barrel = (BlockBarrel) this.blockLocation.getBlock();
        if (!barrel.isOpen()) {
            barrel.setOpen(true);
            barrel.getWorld().setAndUpdateBlock(barrel, barrel.getLocation().toVector3i());
        }
    }

    @Override
    public void showCloseAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showCloseAnimation(player);
        }
    }

    @Override
    public void showCloseAnimation(Player player) {
        BlockBarrel barrel = (BlockBarrel) this.blockLocation.getBlock();
        if (barrel.isOpen() && this.getInventory().getViewers().isEmpty()) {
            barrel.setOpen(false);
            barrel.getWorld().setAndUpdateBlock(barrel, barrel.getLocation().toVector3i());
        }
    }

}
