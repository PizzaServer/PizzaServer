package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.item.Item;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ItemBlock extends Item {

    private final Block block;
    private Set<String> blocksCanPlaceOn = Collections.emptySet();


    public ItemBlock(String blockId) {
        this(blockId, 1);
    }

    public ItemBlock(String blockId, int count) {
        this(blockId, count, 0);
    }

    public ItemBlock(String blockId, int count, int meta) {
        this(BlockRegistry.getInstance().getBlock(blockId), count, meta);
    }

    public ItemBlock(Block block) {
        this(block, 1, block.getStackMeta());
    }

    public ItemBlock(Block block, int count) {
        this(block, count, block.getStackMeta());
    }

    public ItemBlock(Block block, int count, int meta) {
        super(block.getBlockId(), count, meta);
        this.block = block.clone();
        this.block.updateFromStackMeta(meta);
    }

    @Override
    public String getName() {
        return this.block.getName();
    }

    @Override
    public String getItemId() {
        return this.block.getBlockId();
    }

    @Override
    public int getMeta() {
        return this.getBlock().getStackMeta();
    }

    @Override
    public void setMeta(int meta) {
        this.getBlock().updateFromStackMeta(meta);
    }

    public Block getBlock() {
        return this.block;
    }

    public Set<String> getBlocksCanPlaceOn() {
        return Collections.unmodifiableSet(this.blocksCanPlaceOn);
    }

    public void setBlocksCanPlaceOn(Set<String> blocksCanPlaceOn) {
        this.blocksCanPlaceOn = new HashSet<>(blocksCanPlaceOn);
    }

}
