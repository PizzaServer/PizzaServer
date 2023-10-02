package io.github.pizzaserver.server.block.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.PillarAxis;
import io.github.pizzaserver.api.block.impl.BlockStrippableWoodenLikeBlock;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItem;
import io.github.pizzaserver.api.player.Player;

public class StrippableWoodenLikeBlockBehavior extends BaseBlockBehavior<BlockStrippableWoodenLikeBlock> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockStrippableWoodenLikeBlock block, BlockFace face, Vector3f clickPosition) {
        switch (face) {
            case TOP, BOTTOM -> block.setPillarAxis(PillarAxis.Y);
            case EAST, WEST -> block.setPillarAxis(PillarAxis.X);
            case NORTH, SOUTH -> block.setPillarAxis(PillarAxis.Z);
        }

        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

    @Override
    public boolean onInteract(Player player, BlockStrippableWoodenLikeBlock block, BlockFace face, Vector3f clickPosition) {
        if (!block.isStripped()
                && player.getInventory().getHeldItem() instanceof ToolItem tool
                && tool.getToolType() == ToolType.AXE) {
            block.setStripped(true);
            block.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());

            tool.useDurability();
            player.getInventory().setHeldItem(tool);
        }

        return true;
    }

}
