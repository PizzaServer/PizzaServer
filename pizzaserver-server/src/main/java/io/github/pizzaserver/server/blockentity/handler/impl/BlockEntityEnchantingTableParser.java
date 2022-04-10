package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.blockentity.type.BlockEntityEnchantingTable;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityEnchantingTable;

public class BlockEntityEnchantingTableParser extends BaseBlockEntityParser<BlockEntityEnchantingTable> {

    @Override
    public BlockEntityEnchantingTable create(BlockLocation location) {
        return new ImplBlockEntityEnchantingTable(location);
    }

}
