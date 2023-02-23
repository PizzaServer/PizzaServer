package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockCraftingTable;
import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.inventory.ImplCraftingTableInventory;

public class CraftingTableBehavior extends BaseBlockBehavior<BlockCraftingTable> {

    @Override
    public boolean onInteract(Player player, BlockCraftingTable block, BlockFace face, Vector3f clickPosition) {
        if (player.expect(EntityKeys.SNEAKING))
            return super.onInteract(player, block, face, clickPosition);


        player.openInventory(new ImplCraftingTableInventory(block));
        return false;
    }

}
