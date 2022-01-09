package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.descriptors.BlockEntityContainer;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBed;

import java.util.ArrayList;
import java.util.List;

public class BlockBed extends BaseBlock implements BlockEntityContainer<BlockEntityBed> {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int direction = 0; direction <= 3; direction++) {
                for (int headPieceBit = 0; headPieceBit <= 1; headPieceBit++) {
                    for (int occupiedBit = 0; occupiedBit <= 1; occupiedBit++) {
                        this.add(NbtMap.builder()
                                .putInt("direction", direction)
                                .putByte("head_piece_bit", (byte) headPieceBit)
                                .putByte("occupied_bit", (byte) occupiedBit)
                                .build());
                    }
                }
            }
        }
    };


    @Override
    public String getBlockId() {
        return BlockID.BED;
    }

    @Override
    public String getName() {
        return "Bed";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 0.2f;
    }

    @Override
    public float getHardness() {
        return 0.2f;
    }

}
