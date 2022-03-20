package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockCraftingTable;
import io.github.pizzaserver.api.player.Player;

public class CraftingTableBehavior extends DefaultBlockBehavior<BlockCraftingTable> {

    @Override
    public boolean onInteract(Player player, BlockCraftingTable block, BlockFace face, Vector3f clickPosition) {
        if (player.isSneaking()) {
            return super.onInteract(player, block, face, clickPosition);
        }

        return super.onInteract(player, block, face, clickPosition);
    }

}
