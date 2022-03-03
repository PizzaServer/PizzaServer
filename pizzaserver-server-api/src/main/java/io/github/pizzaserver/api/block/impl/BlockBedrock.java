package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.PushResponse;

import java.util.ArrayList;
import java.util.List;

public class BlockBedrock extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder()
                    .putByte("infiniburn_bit", (byte) 0)
                    .build());

            this.add(NbtMap.builder()
                    .putByte("infiniburn_bit", (byte) 1)
                    .build());
        }
    };


    @Override
    public String getBlockId() {
        return BlockID.BEDROCK;
    }

    @Override
    public String getName() {
        return "Bedrock";
    }

    @Override
    public float getHardness() {
        return -1;
    }

    @Override
    public float getBlastResistance() {
        return -1;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public PushResponse getPushResponse() {
        return PushResponse.DENY;
    }

}
