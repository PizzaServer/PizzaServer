package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockColoredTorchRG extends BaseBlock {

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

}
