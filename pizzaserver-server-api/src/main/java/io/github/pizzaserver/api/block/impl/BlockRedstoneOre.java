package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LitType;
import io.github.pizzaserver.api.block.trait.LitTrait;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.Collections;
import java.util.Set;

public class BlockRedstoneOre extends BaseBlock implements LitTrait {

    protected boolean lit;


    public BlockRedstoneOre() {
        this(LitType.UNLIT);
    }

    public BlockRedstoneOre(LitType litType) {
        this.setLit(litType == LitType.LIT);
    }

    @Override
    public void setLit(boolean lit) {
        this.lit = lit;
    }

    @Override
    public boolean isLit() {
        return this.lit;
    }

    @Override
    public String getBlockId() {
        if (this.isLit()) {
            return BlockID.LIT_REDSTONE_ORE;
        } else {
            return BlockID.REDSTONE_ORE;
        }
    }

    @Override
    public String getName() {
        return "Redstone Ore";
    }

    @Override
    public float getHardness() {
        return 3;
    }

    @Override
    public float getBlastResistance() {
        return 3;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.IRON;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemBlock(new BlockRedstoneWire(), (int) Math.floor(Math.random() * 2) + 4));
    }

}
