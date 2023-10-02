package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.BoundingBox;

public class BlockLegacyStoneCutter extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.LEGACY_STONE_CUTTER;
    }

    @Override
    public String getName() {
        return "Stone Cutter";
    }

    @Override
    public float getHardness() {
        return 3.5f;
    }

    @Override
    public float getBlastResistance() {
        return 3.5f;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 0.56250f, 1))
                .translate(this.getLocation().toVector3f());
    }

}
