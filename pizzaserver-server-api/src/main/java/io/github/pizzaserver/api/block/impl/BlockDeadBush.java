package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItem;
import io.github.pizzaserver.api.item.impl.ItemStick;

import java.util.Collections;
import java.util.Set;

public class BlockDeadBush extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.DEADBUSH;
    }

    @Override
    public String getName() {
        return "Dead Bush";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHEARS;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        if (entity.getInventory().getHeldItem() instanceof ToolItem toolItemComponent
                && toolItemComponent.getToolType() == ToolType.SHEARS) {
            return Collections.singleton(this.toItem());
        }

        return Collections.singleton(new ItemStick((int) Math.floor(Math.random() * 2) + 1));
    }

}
