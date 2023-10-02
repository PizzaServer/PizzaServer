package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.utils.HorizontalDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockEnderChest extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .build());
            }
        }
    };


    public BlockEnderChest() {
        this(HorizontalDirection.NORTH);
    }

    public BlockEnderChest(HorizontalDirection direction) {
        this.setDirection(direction);
    }

    public HorizontalDirection getDirection() {
        return HorizontalDirection.fromOmniBlockStateIndex(this.getBlockState());
    }

    public void setDirection(HorizontalDirection direction) {
        this.setBlockState(direction.getOmniBlockStateIndex());
    }

    @Override
    public String getBlockId() {
        return BlockID.ENDER_CHEST;
    }

    @Override
    public String getName() {
        return "Ender Chest";
    }

    @Override
    public float getHardness() {
        return 22.5f;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public int getLightEmission() {
        return 7;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
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
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemBlock(new BlockObsidian(), 8));
    }

}
