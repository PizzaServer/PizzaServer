package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockLegacyStoneCutter;
import io.github.pizzaserver.api.block.impl.BlockStoneCutter;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.inventory.ImplStoneCutterInventory;

public class StoneCutterBlockBehavior extends BaseBlockBehavior<BlockLegacyStoneCutter> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockLegacyStoneCutter block, BlockFace face, Vector3f clickPosition) {
        if (block instanceof BlockStoneCutter) {
            block.setBlockState(entity.getHorizontalDirection().opposite().getOmniBlockStateIndex());
        }

        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

    @Override
    public boolean onInteract(Player player, BlockLegacyStoneCutter block, BlockFace face, Vector3f clickPosition) {
        if (player.isSneaking()) {
            return super.onInteract(player, block, face, clickPosition);
        }

        player.openInventory(new ImplStoneCutterInventory(block));
        return false;
    }

}
