package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BlockPressurePlate extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int redstoneSignal = 0; redstoneSignal < 16; redstoneSignal++) {
                this.add(NbtMap.builder()
                        .putInt("redstone_signal", redstoneSignal)
                        .build());
            }
        }
    };


    public BlockPressurePlate() {
        this(0);
    }

    public BlockPressurePlate(int redstoneSignal) {
        this.setRedstoneSignal(redstoneSignal);
    }

    public boolean isActivated() {
        return this.getBlockState() > 0;
    }

    public int getRedstoneSignal() {
        return this.getBlockState();
    }

    public void setRedstoneSignal(int signal) {
        this.setBlockState(Math.max(0, Math.min(signal, 15)));
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
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return Collections.unmodifiableList(BLOCK_STATES);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 0.0625f, 1))
                .translate(this.getLocation().toVector3f());
    }

}
