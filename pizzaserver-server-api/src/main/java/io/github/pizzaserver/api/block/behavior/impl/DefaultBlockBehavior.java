package io.github.pizzaserver.api.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.item.ItemStack;

import java.util.Collections;
import java.util.Set;

public class DefaultBlockBehavior implements BlockBehavior {

    /**
     * Retrieve the loot that should be dropped when this block is broken.
     * @param entity entity who broke this block
     * @param block the block that was broken
     * @return the
     */
    protected Set<ItemStack> getDrops(Entity entity, Block block) {
        boolean correctTool = entity.getInventory().getHeldItem().getItemType().getToolTier().getStrength() >= block.getToolTierRequired().getStrength()
                && entity.getInventory().getHeldItem().getItemType().getToolType() == block.getToolTypeRequired();

        if (block.canBeMinedWithHand() || correctTool) {
            return Collections.singleton(block.toStack());
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void onBreak(Entity entity, Block block) {
        for (ItemStack loot : this.getDrops(entity, block)) {
            block.getWorld().addItemEntity(loot,
                    block.getLocation().toVector3f().add(0.5f, 0.5f, 0.5f),
                    ItemEntity.getRandomMotion());
        }
    }

}
