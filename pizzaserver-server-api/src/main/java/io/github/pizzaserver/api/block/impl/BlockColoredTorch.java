package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BlockColoredTorch extends Block {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            String[] directions = new String[]{ "unknown", "west", "east", "north", "south", "top" };

            for (String direction : directions) {
                this.add(NbtMap.builder()
                        .putByte("color_bit", (byte) 0)
                        .putString("torch_facing_direction", direction)
                        .build());
            }

            for (String direction : directions) {
                this.add(NbtMap.builder()
                        .putByte("color_bit", (byte) 1)
                        .putString("torch_facing_direction", direction)
                        .build());
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockID.COLORED_TORCH_RG;
    }

    @Override
    public String getName() {
        return "Torch";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

}
