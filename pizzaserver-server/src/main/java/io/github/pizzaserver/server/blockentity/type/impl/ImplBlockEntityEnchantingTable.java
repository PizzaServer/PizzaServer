package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockEnchantingTable;
import io.github.pizzaserver.api.blockentity.type.BlockEntityEnchantingTable;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityEnchantingTable extends BaseBlockEntity<BlockEnchantingTable> implements BlockEntityEnchantingTable {

    public static final Set<String> BLOCK_IDS = Collections.singleton(BlockID.ENCHANTING_TABLE);

    public ImplBlockEntityEnchantingTable(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

}
