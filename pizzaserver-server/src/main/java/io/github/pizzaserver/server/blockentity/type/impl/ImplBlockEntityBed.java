package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockBed;
import io.github.pizzaserver.api.blockentity.type.BlockEntityBed;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.DyeColor;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityBed extends BaseBlockEntity<BlockBed> implements BlockEntityBed {

    protected DyeColor color = DyeColor.WHITE;


    public ImplBlockEntityBed(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.BED);
    }

    @Override
    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(DyeColor color) {
        if (this.color != color) {
            this.color = color;
            this.update();
        }
    }

}
