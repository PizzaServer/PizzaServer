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

    /**
     * Retrieve the loot that should be dropped when this block is broken.
     *
     * @param entity entity who broke this block
     * @param block  the block that was broken
     * @return the
     */
    protected Set<Item> getDrops(Entity entity, Block block) {
        boolean correctTool = false;
        if (entity.getInventory().getHeldItem() instanceof ToolItemComponent itemToolComponent) {
            correctTool = itemToolComponent.getToolTier().getStrength() >= block.getToolTierRequired().getStrength()
                    && itemToolComponent.getToolType() == block.getToolTypeRequired();
        }

        if (block.canBeMinedWithHand() || correctTool) {
            return Collections.singleton(block.toStack());
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void onBreak(Entity entity, Block block) {
        for (Item loot : this.getDrops(entity, block)) {
            block.getWorld()
                 .addItemEntity(loot,
                                block.getLocation().toVector3f().add(0.5f, 0.5f, 0.5f),
                                EntityItem.getRandomMotion());
        }
    }
}
