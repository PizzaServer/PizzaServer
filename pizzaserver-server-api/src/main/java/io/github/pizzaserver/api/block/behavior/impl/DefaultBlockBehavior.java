package io.github.pizzaserver.api.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

import java.util.Collections;
import java.util.Set;

public class DefaultBlockBehavior implements BlockBehavior {

    @Override
    public void onBreak(Entity entity, Block block) {
        boolean correctTool = false;
        if (entity.getInventory().getHeldItem() instanceof ToolItemComponent itemToolComponent) {
            correctTool = itemToolComponent.getToolTier().getStrength() >= block.getToolTierRequired().getStrength()
                    && itemToolComponent.getToolType() == block.getToolTypeRequired();
        }

        if (block.canBeMinedWithHand() || correctTool) {
            for (Item loot : block.getDrops(entity)) {
                block.getWorld().addItemEntity(loot,
                        block.getLocation().toVector3f().add(0.5f, 0.5f, 0.5f),
                        EntityItem.getRandomMotion());
            }
        }
    }

}
