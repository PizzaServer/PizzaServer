package io.github.pizzaserver.server.blockentity.type.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.type.BlockEntityUnknown;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityUnknown<T extends Block> extends BaseBlockEntity<T> implements BlockEntityUnknown<T> {

    private final NbtMap data;

    public ImplBlockEntityUnknown(NbtMap data, BlockLocation location) {
        super(location);
        this.data = data;
    }

    @Override
    public String getId() {
        return this.data.getString("id");
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.emptySet();
    }

    public NbtMap getData() {
        return this.data;
    }

}
