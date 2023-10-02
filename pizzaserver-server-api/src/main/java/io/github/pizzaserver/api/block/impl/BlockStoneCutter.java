package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockStoneCutter extends BlockLegacyStoneCutter {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .build());
            }
        }
    });

    @Override
    public String getBlockId() {
        return BlockID.STONE_CUTTER;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

}
