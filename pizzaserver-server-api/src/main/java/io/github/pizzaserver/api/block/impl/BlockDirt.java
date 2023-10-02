package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.DirtType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockDirt extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder()
                    .putString("dirt_type", "normal")
                    .build());
            this.add(NbtMap.builder()
                    .putString("dirt_type", "coarse")
                    .build());
            this.add(NbtMap.EMPTY);
        }
    };


    public BlockDirt() {
        this(DirtType.NORMAL);
    }

    public BlockDirt(DirtType dirtType) {
        this.setDirtType(dirtType);
    }

    public DirtType getDirtType() {
        return DirtType.values()[this.getBlockState()];
    }

    public void setDirtType(DirtType dirtType) {
        this.setBlockState(dirtType.ordinal());
    }

    @Override
    public String getBlockId() {
        if (this.getDirtType() == DirtType.ROOTED) {
            return BlockID.DIRT_WITH_ROOTS;
        } else {
            return BlockID.DIRT;
        }
    }

    @Override
    public String getName() {
        if (this.getDirtType() == DirtType.ROOTED) {
            return "Rooted Dirt";
        } else {
            return "Dirt";
        }
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getBlastResistance() {
        return 0.1f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

    @Override
    public int getStackMeta() {
        return switch (this.getDirtType()) {
            case NORMAL, ROOTED -> 0;
            case COARSE -> 1;
        };
    }

    @Override
    public void updateFromStackMeta(int meta) {
        if (this.getDirtType() != DirtType.ROOTED && meta >= 0 && meta <= 1) {
            this.setDirtType(DirtType.values()[meta]);
        }
    }

}
