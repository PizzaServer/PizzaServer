package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
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

    public BlockCarvedPumpkin() { this(HorizontalDirection.NORTH); }

    public BlockCarvedPumpkin(HorizontalDirection direction) { this.setDirection(direction); }

    public HorizontalDirection getDirection() {
        return HorizontalDirection.fromBlockStateIndex(this.getBlockState());
    }

    public void setDirection(HorizontalDirection direction) { this.setBlockState(direction.getBlockStateIndex()); }

    @Override
    public String getBlockId(){ return BlockID.CARVED_PUMPKIN; }

    @Override
    public String getName(){ return "Carved Pumpkin"; }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness(){ return 1;}

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
}
