package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockButton extends Block {

    protected static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putByte("button_pressed_bit", (byte) 0)
                        .putInt("facing_direction", i)
                        .build());
            }

            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putByte("button_pressed_bit", (byte) 1)
                        .putInt("facing_direction", i)
                        .build());
            }
        }
    };

    protected final String buttonId;


    public BlockButton(String buttonId) {
        this.buttonId = buttonId;
    }

    @Override
    public String getBlockId() {
        return this.buttonId;
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
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

}
