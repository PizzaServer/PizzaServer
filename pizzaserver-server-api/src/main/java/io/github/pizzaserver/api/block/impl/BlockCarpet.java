package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.trait.FlammableTrait;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.BoundingBox;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class BlockCarpet extends BaseBlock implements FlammableTrait {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (DyeColor color : DyeColor.values()) {
                this.add(NbtMap.builder()
                        .putString("color", color.getId())
                        .build());
            }
        }
    };

    public BlockCarpet() {
        this(DyeColor.WHITE);
    }

    public BlockCarpet(DyeColor color) {
        this.setColor(color);
    }

    public DyeColor getColor() {
        return DyeColor.values()[this.getBlockState()];
    }

    public void setColor(DyeColor color) {
        this.setBlockState(color.ordinal());
    }

    @Override
    public String getBlockId() {
        return BlockID.CARPET;
    }

    @Override
    public String getName() {
        return "Carpet";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.1f;
    }

    @Override
    public float getBlastResistance() {
        return 0.1f;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 0.0625f, 1))
                .translate(this.getLocation().toVector3f());
    }

    @Override
    public boolean isTransparent() {
        return true;
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
    public int getBurnOdds() {
        return 30;
    }

    @Override
    public int getFlameOdds() {
        return 60;
    }

    @Override
    public int getStackMeta() {
        return this.getBlockState();
    }

    @Override
    public void updateFromStackMeta(int meta) {
        if (meta >= 0 && meta < DyeColor.values().length) {
            this.setBlockState(meta);
        }
    }

}
