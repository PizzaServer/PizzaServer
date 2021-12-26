package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

import java.util.HashMap;

public class BlockTypeBed extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            int blockStateIndex = 0;
            for (int direction = 0; direction <= 3; direction++) {
                for (int headPieceBit = 0; headPieceBit <= 1; headPieceBit++) {
                    for (int occupiedBit = 0; occupiedBit <= 1; occupiedBit++) {
                        this.put(NbtMap.builder()
                                        .putInt("direction", direction)
                                        .putByte("head_piece_bit", (byte) headPieceBit)
                                        .putByte("occupied_bit", (byte) occupiedBit)
                                .build(), blockStateIndex++);
                    }
                }
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.BED;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Bed";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 0.2f;
    }

    @Override
    public float getHardness(int blockStateIndex) {
        return 0.2f;
    }

}
