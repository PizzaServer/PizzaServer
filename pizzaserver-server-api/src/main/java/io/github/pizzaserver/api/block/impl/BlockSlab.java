package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.utils.BoundingBox;

public abstract class BlockSlab extends Block {

    public boolean isUpperSlab() {
        return this.getBlockState() == 1;
    }

    public void setUpperSlab(boolean isUpper) {
        this.setBlockState(isUpper ? 1 : 0);
    }

    @Override
    public BoundingBox getBoundingBox() {
        if (this.isUpperSlab()) {
            return new BoundingBox(Vector3f.from(0, 0.5f, 0), Vector3f.from(1, 1, 1));
        } else {
            return new BoundingBox(Vector3f.from(0, 0, 0), Vector3f.from(1, 0.5f, 1));
        }
    }

}
