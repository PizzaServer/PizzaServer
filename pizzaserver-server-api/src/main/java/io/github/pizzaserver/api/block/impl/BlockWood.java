package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.PillarAxis;
import io.github.pizzaserver.api.block.data.StrippedType;
import io.github.pizzaserver.api.block.data.WoodType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockWood extends BlockStrippableWoodenLikeBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            // These 3 block states are used for crimson/warped wood blocks.
            this.add(NbtMap.builder()
                    .putString("pillar_axis", "x")
                    .build());
            this.add(NbtMap.builder()
                    .putString("pillar_axis", "y")
                    .build());
            this.add(NbtMap.builder()
                    .putString("pillar_axis", "z")
                    .build());

            // These block states are only used for all other wood blocks.
            Arrays.asList("oak", "spruce", "birch", "jungle", "acacia", "dark_oak").forEach(woodType ->
                    Arrays.asList("x", "y", "z").forEach(axis -> {
                        this.add(NbtMap.builder()
                                .putString("wood_type", woodType)
                                .putByte("stripped_bit", (byte) 0)
                                .putString("pillar_axis", axis)
                                .build());
                        this.add(NbtMap.builder()
                                .putString("wood_type", woodType)
                                .putByte("stripped_bit", (byte) 1)
                                .putString("pillar_axis", axis)
                                .build());
                    }));
        }
    };

    public BlockWood() {
        this(WoodType.OAK);
    }

    public BlockWood(WoodType woodType) {
        this(woodType, StrippedType.UNSTRIPPED);
    }

    public BlockWood(WoodType woodType, StrippedType strippedType) {
        this(woodType, strippedType, PillarAxis.Y);
    }

    public BlockWood(WoodType woodType, PillarAxis axis) {
        this(woodType, StrippedType.UNSTRIPPED, axis);
    }

    public BlockWood(WoodType woodType, StrippedType strippedType, PillarAxis axis) {
        super(woodType, strippedType, axis);
    }

    @Override
    public void setWoodType(WoodType woodType) {
        PillarAxis axis = this.getPillarAxis();
        super.setWoodType(woodType);

        switch (woodType) {
            case OAK:
            case SPRUCE:
            case BIRCH:
            case JUNGLE:
            case ACACIA:
            case DARK_OAK:
                this.setBlockState(3 + (this.getWoodType().ordinal() * 6) + (axis.ordinal() * 2) + (this.isStripped() ? 1 : 0));
                break;
            case CRIMSON:
            case WARPED:
                this.setBlockState(axis.ordinal());
                break;
        }
    }

    @Override
    public void setStripped(boolean stripped) {
        if (this.isStripped() != stripped) {
            PillarAxis axis = this.getPillarAxis();
            super.setStripped(stripped);

            if (this.getWoodType() != WoodType.CRIMSON && this.getWoodType() != WoodType.WARPED) {
                this.setBlockState(3 + (this.getWoodType().ordinal() * 6) + (axis.ordinal() * 2) + (stripped ? 1 : 0));
            }
        }
    }

    @Override
    public PillarAxis getPillarAxis() {
        if (this.getBlockState() < 3) {
            return PillarAxis.values()[this.getBlockState()];
        } else {
            int axisIndex = (this.getBlockState() - 3 - (this.getWoodType().ordinal() * 6) - (this.isStripped() ? 1 : 0)) / 2;
            return PillarAxis.values()[axisIndex];
        }
    }

    @Override
    public void setPillarAxis(PillarAxis pillarAxis) {
        if (this.getWoodType() == WoodType.CRIMSON || this.getWoodType() == WoodType.WARPED) {
            this.setBlockState(pillarAxis.ordinal());
        } else {
            this.setBlockState(3 + (this.getWoodType().ordinal() * 6) + (pillarAxis.ordinal() * 2) + (this.isStripped() ? 1 : 0));
        }
    }

    @Override
    public String getBlockId() {
        if (this.isStripped()) {
            return switch (this.getWoodType()) {
                case OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK -> BlockID.WOOD;
                case CRIMSON -> BlockID.STRIPPED_CRIMSON_HYPHAE;
                case WARPED -> BlockID.STRIPPED_WARPED_HYPHAE;
            };
        } else {
            return switch (this.getWoodType()) {
                case OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK -> BlockID.WOOD;
                case CRIMSON -> BlockID.CRIMSON_HYPHAE;
                case WARPED -> BlockID.WARPED_HYPHAE;
            };
        }
    }

    @Override
    public String getName() {
        return switch (this.getWoodType()) {
            case CRIMSON, WARPED -> this.getWoodType().getName() + " Hyphae";
            default -> this.getWoodType().getName() + " Wood";
        };
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return Collections.unmodifiableList(BLOCK_STATES);
    }

    @Override
    public void setBlockState(int index) {
        switch (this.getWoodType()) {
            case OAK:
            case SPRUCE:
            case BIRCH:
            case JUNGLE:
            case ACACIA:
            case DARK_OAK:
                if (index < 3 || index > 36) {
                    this.setBlockState(3 + (this.getWoodType().ordinal() * 6) + (this.getPillarAxis().ordinal() * 2) + (index % 2));
                    return;
                }
                this.woodType = WoodType.values()[(int) Math.floor((index - 3) / 6d)];
                this.stripped = ((index - 3) - (this.getWoodType().ordinal() * 6)) % 2 == 1;
                break;
            case CRIMSON:
            case WARPED:
                if (index < 0 || index > 3) {
                    this.setBlockState(index % 3);
                    return;
                }
                break;
        }
        super.setBlockState(index);
    }

    @Override
    public int getStackMeta() {
        return switch (this.getWoodType()) {
            case CRIMSON, WARPED -> 0;
            default -> this.isStripped() ? 8 + this.getWoodType().ordinal() : this.getWoodType().ordinal();
        };
    }

    @Override
    public void updateFromStackMeta(int meta) {
        if (this.getBlockId().equals(BlockID.WOOD)) {
            if (meta >= 8) {
                this.setWoodType(WoodType.values()[meta - 8]);
                this.setStripped(true);
            } else {
                this.setWoodType(WoodType.values()[meta]);
            }
        }
    }

}
