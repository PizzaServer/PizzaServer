package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.FurnaceType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.List;

public class BlockFurnace extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int direction = 0; direction < 6; direction++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", direction)
                        .build());
            }
        }
    };

    private boolean lit;


    public BlockFurnace() {
        this(FurnaceType.UNLIT);
    }

    public BlockFurnace(FurnaceType furnaceType) {
        this.setLit(furnaceType == FurnaceType.LIT);
    }

    public boolean isLit() {
        return this.lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
    }

    @Override
    public String getBlockId() {
        if (this.isLit()) {
            return BlockID.LIT_FURNACE;
        } else {
            return BlockID.FURNACE;
        }
    }

    @Override
    public String getName() {
        if (this.isLit()) {
            return "Lit Furnace";
        } else {
            return "Furnace";
        }
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 3.5f;
    }

    @Override
    public float getHardness() {
        return 3.5f;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

}
