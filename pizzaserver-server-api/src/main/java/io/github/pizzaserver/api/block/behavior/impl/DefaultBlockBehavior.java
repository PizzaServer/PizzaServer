package io.github.pizzaserver.api.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.descriptors.ToolItem;
import io.github.pizzaserver.api.player.Player;

import java.util.Set;

public class DefaultBlockBehavior<T extends Block> implements BlockBehavior<T> {

    @Override
    public boolean prepareForPlacement(Entity entity, T block, BlockFace face, Vector3f clickPosition) {
        if (this.collideWithEntities(entity, block)) {
            return false;
        }

        return this.canReplaceOldBlockWithNewBlock(block);
    }

    protected boolean canReplaceOldBlockWithNewBlock(T block) {
        Block replacedBlock = block.getWorld().getBlock(block.getLocation().toVector3i());
        return replacedBlock.isAir() || replacedBlock.isReplaceable();
    }

    protected boolean collideWithEntities(Entity entity, T block) {
        if (block.hasCollision()) {
            // Collision check with nearby entities
            Set<Entity> nearByEntities = block.getLocation().getWorld().getEntitiesNear(block.getLocation().toVector3f(), 16);
            for (Entity nearbyEntity : nearByEntities) {
                boolean entityCollidesWithBlock = block.getBoundingBox().collidesWith(nearbyEntity.getBoundingBox())
                        && nearbyEntity.hasCollision()
                        && !(nearbyEntity instanceof EntityItem)
                        && ((entity instanceof Player && nearbyEntity.getViewers().contains((Player) entity))
                        || nearbyEntity.equals(entity));

                if (entityCollidesWithBlock) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onBreak(Entity entity, T block) {
        boolean correctTool = false;
        if (entity.getInventory().getHeldItem() instanceof ToolItem itemToolComponent) {
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
