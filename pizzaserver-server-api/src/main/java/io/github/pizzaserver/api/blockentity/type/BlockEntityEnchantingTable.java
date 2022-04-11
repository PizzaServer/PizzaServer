package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockEnchantingTable;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityEnchantingTable extends BlockEntity<BlockEnchantingTable> {

    String ID = "EnchantTable";

    @Override
    default String getId() {
        return ID;
    }

}
