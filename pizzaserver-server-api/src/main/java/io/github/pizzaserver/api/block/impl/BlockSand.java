package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.SandType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockSand extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder()
                    .putString("sand_type", "normal")
                    .build());
            this.add(NbtMap.builder()
                    .putString("sand_type", "red")
                    .build());
        }
    };


    public BlockSand() {
        this(SandType.NORMAL);
    }

    public BlockSand(SandType sandType) {
        this.setBlockState(sandType.ordinal());
    }

    public SandType getSandType() {
        return SandType.values()[this.getBlockState()];
    }

    public void setSandType(SandType sandType) {
        this.setBlockState(sandType.ordinal());
    }

    @Override
    public String getBlockId() {
        return BlockID.SAND;
    }

    @Override
    public String getName() {
        if (this.getBlockState() == 0) {
            return "Sand";
        } else {
            return "Red Sand";
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
        return 0.5f;
    }

    @Override
    public boolean isAffectedByGravity() {
        return true;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ItemBlock toStack() {
        return new ItemBlock(this.getBlockId(), 1,  this.getBlockState());
    }

    @Override
    public int getStackMeta() {
        return this.getBlockState();
    }

    @Override
    public void updateFromStackMeta(int meta) {
        if (meta >= 0 && meta <= 1) {
            this.setBlockState(meta);
        }
    }

}
