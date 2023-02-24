package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.trait.FlammableTrait;
import io.github.pizzaserver.api.block.trait.WoodVariantTrait;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockPlanks extends BaseBlock implements FlammableTrait, WoodVariantTrait {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            this.add(NbtMap.EMPTY);
            String[] woodTypes = { "oak", "spruce", "birch", "jungle", "acacia", "dark_oak" };
            for (String woodType : woodTypes) {
                this.add(NbtMap.builder()
                        .putString("wood_type", woodType)
                        .build());
            }
        }
    });

    protected WoodType woodType;

    public BlockPlanks() {
        this(WoodType.OAK);
    }

    public BlockPlanks(WoodType woodType) {
        this.setWoodType(woodType);
    }

    @Override
    public String getBlockId() {
        return switch (this.getWoodType()) {
            case CRIMSON -> BlockID.CRIMSON_PLANKS;
            case WARPED -> BlockID.WARPED_PLANKS;
            default -> BlockID.PLANKS;
        };
    }

    @Override
    public String getName() {
        return "Planks";
    }

    @Override
    public float getHardness() {
        return 2;
    }

    @Override
    public float getBlastResistance() {
        return 3;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public int getBurnOdds() {
        return 5;
    }

    @Override
    public int getFlameOdds() {
        return 5;
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }

    @Override
    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;

        switch (woodType) {
            case CRIMSON, WARPED -> this.setBlockState(0);
            default -> this.setBlockState(woodType.ordinal() + 1);
        }
    }

    @Override
    public int getStackMeta() {
        return switch (this.getWoodType()) {
            case CRIMSON, WARPED -> 0;
            default -> this.getBlockState() - 1;
        };
    }

    @Override
    public void updateFromStackMeta(int meta) {
        if (this.getWoodType() != WoodType.CRIMSON && this.getWoodType() != WoodType.WARPED) {
            this.setBlockState(Math.max(0, Math.min(meta, 5)) + 1);
        }
    }
}
