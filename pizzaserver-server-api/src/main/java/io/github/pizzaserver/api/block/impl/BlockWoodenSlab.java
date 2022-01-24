package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.descriptors.Flammable;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockWoodenSlab extends BlockSlab implements Flammable {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder()
                    .putByte("top_slot_bit", (byte) 0)
                    .build());

            this.add(NbtMap.builder()
                    .putByte("top_slot_bit", (byte) 1)
                    .build());

            Arrays.asList("oak", "spruce", "birch", "jungle", "acacia", "dark_oak").forEach(woodType -> {
                this.add(NbtMap.builder()
                        .putByte("top_slot_bit", (byte) 0)
                        .putString("wood_type", woodType)
                        .build());

                this.add(NbtMap.builder()
                        .putByte("top_slot_bit", (byte) 1)
                        .putString("wood_type", woodType)
                        .build());
            });
        }
    };

    private WoodType woodType;


    public BlockWoodenSlab() {
        this(WoodType.OAK);
    }

    public BlockWoodenSlab(SlabType slabType) {
        this(WoodType.OAK, slabType);
    }

    public BlockWoodenSlab(WoodType woodType) {
        this(woodType, SlabType.SINGLE);
    }

    public BlockWoodenSlab(WoodType woodType, SlabType slabType) {
        super(slabType);
        this.setWoodType(woodType);
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
        switch (woodType) {
            case OAK:
            case SPRUCE:
            case BIRCH:
            case JUNGLE:
            case ACACIA:
            case DARK_OAK:
                this.setBlockState(2 + (woodType.ordinal() * 2 + (this.isUpperSlab() ? 1 : 0)));
                break;
            case CRIMSON:
            case WARPED:
                this.setBlockState(this.isUpperSlab() ? 1 : 0);
                break;
        }
    }

    @Override
    public void setBlockState(int index) {
        // Prevent the block state from going outside its range since Mojang has different block states for crimson/warped
        boolean wantsUpperSlab = index % 2 == 1;
        switch (this.getWoodType()) {
            case OAK:
            case SPRUCE:
            case BIRCH:
            case JUNGLE:
            case ACACIA:
            case DARK_OAK:
                if (index <= 1) {
                    this.setBlockState(this.getWoodType().ordinal() * 2 + 2 + (wantsUpperSlab ? 1 : 0));
                    return;
                }
                this.woodType = WoodType.values()[(int) Math.floor((index - 2) / 2d)];
                break;
            case CRIMSON:
            case WARPED:
                if (index >= 2) {
                    this.setBlockState(wantsUpperSlab ? 1 : 0);
                    return;
                }
                break;
        }
        super.setBlockState(index);
    }

    @Override
    public boolean isUpperSlab() {
        return switch (this.getWoodType()) {
            case OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK -> this.getBlockState() % 2 == 1;
            case CRIMSON, WARPED -> super.isUpperSlab();
        };
    }

    @Override
    public void setUpperSlab(boolean isUpper) {
        switch (this.getWoodType()) {
            case OAK:
            case SPRUCE:
            case BIRCH:
            case JUNGLE:
            case ACACIA:
            case DARK_OAK:
                if (isUpper) {
                    this.setBlockState(2 + (this.getWoodType().ordinal() * 2 + 1));
                } else {
                    this.setBlockState(2 + (this.getWoodType().ordinal() * 2));
                }
                break;
            case CRIMSON:
            case WARPED:
                super.setUpperSlab(isUpper);
                break;
        }
    }

    @Override
    public String getBlockId() {
        return switch (this.getWoodType()) {
            case OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK -> this.isDouble() ? BlockID.DOUBLE_WOODEN_SLAB : BlockID.WOODEN_SLAB;
            case CRIMSON -> this.isDouble() ? BlockID.DOUBLE_CRIMSON_SLAB : BlockID.CRIMSON_SLAB;
            case WARPED -> this.isDouble() ? BlockID.DOUBLE_WARPED_SLAB : BlockID.WARPED_SLAB;
        };
    }

    @Override
    public String getName() {
        return this.getWoodType().getName() + " Slab";
    }

    @Override
    public float getHardness() {
        return 2;
    }

    @Override
    public float getBlastResistance() {
        return 3;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public int getBurnOdds() {
        return 5;
    }

    @Override
    public int getFlameOdds() {
        return 20;
    }

    @Override
    public int getStackMeta() {
        return switch (this.getWoodType()) {
            case OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK -> this.getWoodType().ordinal();
            default -> 0;
        };
    }

    @Override
    public void updateFromStackMeta(int meta) {
        boolean canUpdateMeta = this.getWoodType() != WoodType.CRIMSON
                && this.getWoodType() != WoodType.WARPED
                && meta >= 0
                && meta <= 5;
        if (canUpdateMeta) {
            this.setWoodType(WoodType.values()[meta]);
        }
    }

}
