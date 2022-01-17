package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockSlab extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder()
                    .putByte("top_slot_bit", (byte) 0)
                    .build());

            this.add(NbtMap.builder()
                    .putByte("top_slot_bit", (byte) 1)
                    .build());
        }
    };

    protected boolean doubleSlabs;

    public BlockSlab(SlabType slabType) {
        this.setDouble(slabType == SlabType.DOUBLE);
    }

    public boolean isUpperSlab() {
        return this.getBlockState() == 1;
    }

    public void setUpperSlab(boolean isUpper) {
        this.setBlockState(isUpper ? 1 : 0);
    }

    public boolean isDouble() {
        return this.doubleSlabs;
    }

    public void setDouble(boolean isDouble) {
        this.doubleSlabs = isDouble;
    }

    @Override
    public BoundingBox getBoundingBox() {
        if (this.isDouble()) {
            return super.getBoundingBox();
        }

        if (this.isUpperSlab()) {
            return new BoundingBox(Vector3f.from(0, 0.5f, 0), Vector3f.from(1, 1, 1));
        } else {
            return new BoundingBox(Vector3f.from(0, 0, 0), Vector3f.from(1, 0.5f, 1));
        }
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public ItemBlock toStack() {
        ItemBlock itemBlock = super.toStack();
        itemBlock.setCount(this.isDouble() ? 2 : 1);
        return itemBlock;
    }

}
