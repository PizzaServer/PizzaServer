package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.block.data.StoneSlabType;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockStoneSlab extends BlockSlab {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            addStoneStates("stone_slab_type", Arrays.asList("smooth_stone", "sandstone", "wood", "cobblestone", "brick", "stone_brick", "quartz", "nether_brick"));
            addStoneStates("stone_slab_type_2", Arrays.asList("red_sandstone", "purpur", "prismarine_rough", "prismarine_dark", "prismarine_brick", "mossy_cobblestone",
                    "smooth_sandstone", "red_nether_brick"));
            addStoneStates("stone_slab_type_3", Arrays.asList("end_stone_brick", "smooth_red_sandstone", "polished_andesite", "andesite", "diorite", "polished_diorite",
                    "granite", "polished_granite"));
            addStoneStates("stone_slab_type_4", Arrays.asList("mossy_stone_brick", "smooth_quartz", "stone", "cut_sandstone", "cut_red_sandstone"));
        }
    };

    public BlockStoneSlab() {
        this(StoneSlabType.STONE);
    }

    public BlockStoneSlab(StoneSlabType stoneSlabType) {
        this(stoneSlabType, SlabType.SINGLE);
    }

    public BlockStoneSlab(SlabType slabType) {
        this(StoneSlabType.STONE, slabType);
    }

    public BlockStoneSlab(StoneSlabType stoneSlabType, SlabType slabType) {
        super(slabType);
        this.setStoneType(stoneSlabType);
    }

    public StoneSlabType getStoneType() {
        return StoneSlabType.values()[(this.getBlockState() - (this.getBlockState() % 2)) / 2];
    }

    public void setStoneType(StoneSlabType stoneType) {
        this.setBlockState(stoneType.ordinal() * 2 + (this.isUpperSlab() ? 1 : 0));
    }

    @Override
    public String getBlockId() {
        return switch (this.getStoneType()) {
            case SMOOTH_STONE, SANDSTONE, WOOD, COBBLESTONE, BRICK, STONE_BRICK, QUARTZ, NETHER_BRICK
                    -> this.isDouble() ? BlockID.DOUBLE_STONE_SLAB : BlockID.STONE_SLAB;
            case RED_SANDSTONE, PURPUR, PRISMARINE_ROUGH, PRISMARINE_DARK, PRISMARINE_BRICK, MOSSY_COBBLESTONE, SMOOTH_SANDSTONE, RED_NETHER_BRICK
                    -> this.isDouble() ? BlockID.DOUBLE_STONE_SLAB2 : BlockID.STONE_SLAB2;
            case END_STONE_BRICK, SMOOTH_RED_SANDSTONE, POLISHED_ANDESITE, ANDESITE, DIORITE, POLISHED_DIORITE, GRANITE, POLISHED_GRANITE
                    -> this.isDouble() ? BlockID.DOUBLE_STONE_SLAB3 : BlockID.STONE_SLAB3;
            case MOSSY_STONE_BRICK, SMOOTH_QUARTZ, STONE, CUT_SANDSTONE, CUT_RED_SANDSTONE
                    -> this.isDouble() ? BlockID.DOUBLE_STONE_SLAB4 : BlockID.STONE_SLAB4;
        };
    }

    @Override
    public String getName() {
        return this.getStoneType().getDisplayName();
    }

    @Override
    public float getHardness() {
        return switch (this.getStoneType()) {
            case END_STONE_BRICK -> 3;
            case STONE_BRICK, BRICK, COBBLESTONE, MOSSY_COBBLESTONE, NETHER_BRICK, RED_NETHER_BRICK, PURPUR,
                    SMOOTH_QUARTZ, QUARTZ, CUT_SANDSTONE, SMOOTH_SANDSTONE, RED_SANDSTONE, SANDSTONE,
                    CUT_RED_SANDSTONE, SMOOTH_RED_SANDSTONE, SMOOTH_STONE, STONE, WOOD -> 2;
            case MOSSY_STONE_BRICK, ANDESITE, POLISHED_ANDESITE, GRANITE, POLISHED_GRANITE, DIORITE, POLISHED_DIORITE,
                    PRISMARINE_BRICK, PRISMARINE_DARK, PRISMARINE_ROUGH -> 1.5f;
        };
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public ItemBlock toStack() {
        return switch (this.getStoneType()) {
            case SMOOTH_STONE, SANDSTONE, WOOD, COBBLESTONE, BRICK, STONE_BRICK, QUARTZ, NETHER_BRICK
                    -> new ItemBlock(this.getBlockId(), this.isDouble() ? 2 : 1, this.getStoneType().ordinal());
            case RED_SANDSTONE, PURPUR, PRISMARINE_ROUGH, PRISMARINE_DARK, PRISMARINE_BRICK, MOSSY_COBBLESTONE, SMOOTH_SANDSTONE, RED_NETHER_BRICK
                    -> new ItemBlock(this.getBlockId(), this.isDouble() ? 2 : 1, this.getStoneType().ordinal() - 8);
            case END_STONE_BRICK, SMOOTH_RED_SANDSTONE, POLISHED_ANDESITE, ANDESITE, DIORITE, POLISHED_DIORITE, GRANITE, POLISHED_GRANITE
                    -> new ItemBlock(this.getBlockId(), this.isDouble() ? 2 : 1, this.getStoneType().ordinal() - 16);
            case MOSSY_STONE_BRICK, SMOOTH_QUARTZ, STONE, CUT_SANDSTONE, CUT_RED_SANDSTONE
                    -> new ItemBlock(this.getBlockId(), this.isDouble() ? 2 : 1, this.getStoneType().ordinal() - 24);
        };
    }

    private static void addStoneStates(String typePropertyName, List<String> stoneTypes) {
        for (String stoneType : stoneTypes) {
            BLOCK_STATES.add(NbtMap.builder()
                    .putString(typePropertyName, stoneType)
                    .putByte("top_slot_bit", (byte) 0)
                    .build());

            BLOCK_STATES.add(NbtMap.builder()
                    .putString(typePropertyName, stoneType)
                    .putByte("top_slot_bit", (byte) 1)
                    .build());
        }
    }

}
