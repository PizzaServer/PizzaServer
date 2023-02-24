package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockWoodenPressurePlate extends BlockPressurePlate {

    private WoodType woodType;


    public BlockWoodenPressurePlate() {
        this(WoodType.OAK, 0);
    }

    public BlockWoodenPressurePlate(WoodType woodType) {
        this(woodType, 0);
    }

    public BlockWoodenPressurePlate(int redstoneSignal) {
        this(WoodType.OAK, redstoneSignal);
    }

    public BlockWoodenPressurePlate(WoodType woodType, int redstoneSignal) {
        super(redstoneSignal);
        this.setWoodType(woodType);
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
    }

    @Override
    public String getBlockId() {
        return switch (this.getWoodType()) {
            case OAK -> BlockID.WOODEN_PRESSURE_PLATE;
            case SPRUCE -> BlockID.SPRUCE_PRESSURE_PLATE;
            case BIRCH -> BlockID.BIRCH_PRESSURE_PLATE;
            case JUNGLE -> BlockID.JUNGLE_PRESSURE_PLATE;
            case ACACIA -> BlockID.ACACIA_PRESSURE_PLATE;
            case DARK_OAK -> BlockID.DARK_OAK_PRESSURE_PLATE;
            case CRIMSON -> BlockID.CRIMSON_PRESSURE_PLATE;
            case WARPED -> BlockID.WARPED_PRESSURE_PLATE;
        };
    }

    @Override
    public String getName() {
        return this.getWoodType().getName() + " Pressure Plate";
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
    public int getFuelTicks() {
        return 300;
    }

}
