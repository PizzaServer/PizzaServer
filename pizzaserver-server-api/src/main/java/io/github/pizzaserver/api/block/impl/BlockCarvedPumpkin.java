package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LitType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.HorizontalDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockCarvedPumpkin extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int i = 0; i < 4; i++) {
                this.add(NbtMap.builder()
                        .putInt("direction", i)
                        .build());
            }
        }
    };

    private boolean lit;

    public BlockCarvedPumpkin() {
        this(LitType.UNLIT);
    }

    public BlockCarvedPumpkin(LitType litType) {
        this(litType, HorizontalDirection.NORTH);
    }

    public BlockCarvedPumpkin(LitType litType, HorizontalDirection direction) {
        this.setDirection(direction);
        this.setLit(litType == LitType.LIT);
    }

    public HorizontalDirection getDirection() {
        return HorizontalDirection.fromBlockStateIndex(this.getBlockState());
    }

    public void setDirection(HorizontalDirection direction) {
        this.setBlockState(direction.getBlockStateIndex());
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
            return BlockID.LIT_PUMPKIN;
        }
        return BlockID.CARVED_PUMPKIN;
    }

    @Override
    public String getName() {
        if (this.isLit()) {
            return "Jack O' Lantern";
        }
        return "Carved Pumpkin";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 1;
    }

    @Override
    public float getBlastResistance() {
        return 1;
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
    public int getLightEmission() {
        if (this.isLit()) {
            return 15;
        }
        return 0;
    }
}
