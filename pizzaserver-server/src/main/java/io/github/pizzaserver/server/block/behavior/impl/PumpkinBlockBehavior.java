package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockCarvedPumpkin;
import io.github.pizzaserver.api.block.impl.BlockPumpkin;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItem;
import io.github.pizzaserver.api.item.impl.ItemPumpkinSeeds;
import io.github.pizzaserver.api.player.Player;

public class PumpkinBlockBehavior extends DefaultBlockBehavior<BlockPumpkin> {

    @Override
    public boolean onInteract(Player player, BlockPumpkin pumpkin, BlockFace face, Vector3f clickPosition){
        if (player.getInventory().getHeldItem() instanceof ToolItem toolItem && toolItem.getToolType() == ToolType.SHEARS){
            if (face == BlockFace.TOP || face  == BlockFace.BOTTOM) {
                return true;
            }

            EntityItem itemEntity = EntityRegistry.getInstance().getItemEntity(new ItemPumpkinSeeds());

            pumpkin.getWorld().addEntity(itemEntity,pumpkin.getLocation().toVector3f());
            pumpkin.getWorld().setAndUpdateBlock(new BlockCarvedPumpkin(face.toDirection().toHorizontal()), pumpkin.getLocation().toVector3i());
        }
        return true;
    }
}