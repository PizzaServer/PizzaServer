package io.github.pizzaserver.server.item.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockSign;
import io.github.pizzaserver.api.block.impl.BlockStandingSign;
import io.github.pizzaserver.api.block.impl.BlockWallSign;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.player.Player;

public class ItemBlockSignBehavior extends ItemBlockBehavior {

    @Override
    public boolean onInteract(Player player, ItemBlock itemBlock, Block block, BlockFace blockFace, Vector3f clickPosition) {
        BlockSign sign = (BlockSign) itemBlock.getBlock();
        if (blockFace == BlockFace.TOP) {
            return super.onInteract(player, new ItemBlock(new BlockStandingSign(sign.getWoodType())), block, blockFace, clickPosition);
        } else {
            return super.onInteract(player, new ItemBlock(new BlockWallSign(sign.getWoodType())), block, blockFace, clickPosition);
        }
    }

}
