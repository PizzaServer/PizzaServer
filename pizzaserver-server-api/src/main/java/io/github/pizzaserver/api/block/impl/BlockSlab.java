package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class BlockSlab extends BaseBlock {

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
            return new BoundingBox(Vector3f.from(0, 0.5f, 0), Vector3f.from(1, 1, 1))
                    .translate(this.getLocation().toVector3f());
        } else {
            return new BoundingBox(Vector3f.from(0, 0, 0), Vector3f.from(1, 0.5f, 1))
                    .translate(this.getLocation().toVector3f());
        }
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        ItemBlock clonedBlockStack = this.toStack();
        ((BlockSlab) clonedBlockStack.getBlock()).setUpperSlab(false);
        if (this.isDouble()) {
            clonedBlockStack.setCount(2);
            ((BlockSlab) clonedBlockStack.getBlock()).setDouble(false);
            return Collections.singleton(clonedBlockStack);
        } else {
            return super.getDrops(entity);
        }
    }

}
