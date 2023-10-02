package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.blockentity.type.BlockEntityEnchantingTable;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.BoundingBox;

public class BlockEnchantingTable extends BlockBlockEntity<BlockEntityEnchantingTable> {

    @Override
    public String getBlockId() {
        return BlockID.ENCHANTING_TABLE;
    }

    @Override
    public String getName() {
        return "Enchanting Table";
    }

    @Override
    public float getHardness() {
        return 5;
    }

    @Override
    public float getBlastResistance() {
        return 1200;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 0.75f, 1))
                .translate(this.getLocation().toVector3f());
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.DIAMOND;
    }

}
