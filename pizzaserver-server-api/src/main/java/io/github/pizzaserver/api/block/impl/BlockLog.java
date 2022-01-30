package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.PillarAxis;
import io.github.pizzaserver.api.block.data.StrippedType;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.descriptors.Flammable;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockLog extends Block implements Flammable {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            // These 3 block states are used for crimson/warped stem + all stripped logs.
            this.add(NbtMap.builder()
                    .putString("pillar_axis", "x")
                    .build());
            this.add(NbtMap.builder()
                    .putString("pillar_axis", "y")
                    .build());
            this.add(NbtMap.builder()
                    .putString("pillar_axis", "z")
                    .build());

            // These block states are only used for the unstripped logs ranging from oak to dark oak.
            Arrays.asList("oak", "spruce", "birch", "jungle").forEach(oldLogType ->
                    Arrays.asList("x", "y", "z").forEach(axis -> this.add(NbtMap.builder()
                        .putString("old_log_type", oldLogType)
                        .putString("pillar_axis", axis)
                        .build())));

            Arrays.asList("acacia", "dark_oak").forEach(newLogType ->
                    Arrays.asList("x", "y", "z").forEach(axis -> this.add(NbtMap.builder()
                            .putString("new_log_type", newLogType)
                            .putString("pillar_axis", axis)
                            .build())));
        }
    };

    private WoodType woodType;
    private boolean stripped;


    public BlockLog() {
        this(WoodType.OAK);
    }

    public BlockLog(WoodType woodType) {
        this(woodType, StrippedType.UNSTRIPPED);
    }

    public BlockLog(WoodType woodType, StrippedType strippedType) {
        this(woodType, strippedType, PillarAxis.Y);
    }

    public BlockLog(WoodType woodType, PillarAxis axis) {
        this(woodType, StrippedType.UNSTRIPPED, axis);
    }

    public BlockLog(WoodType woodType, StrippedType strippedType, PillarAxis axis) {
        this.setWoodType(woodType);
        this.setStripped(strippedType == StrippedType.STRIPPED);
        this.setPillarAxis(axis);
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    public void setWoodType(WoodType woodType) {
        PillarAxis axis = this.getPillarAxis();
        this.woodType = woodType;

        switch (woodType) {
            case OAK:
            case SPRUCE:
            case BIRCH:
            case JUNGLE:
            case ACACIA:
            case DARK_OAK:
                this.setBlockState(3 + (this.getWoodType().ordinal() * 3) + axis.ordinal());
                break;
            case CRIMSON:
            case WARPED:
                this.setBlockState(axis.ordinal());
                break;
        }
    }

    public boolean isStripped() {
        return this.stripped;
    }

    public void setStripped(boolean stripped) {
        if (this.stripped != stripped) {
            PillarAxis axis = this.getPillarAxis();
            this.stripped = stripped;

            if (stripped) {
                // unstripped to stripped
                if (this.getWoodType() != WoodType.CRIMSON && this.getWoodType() != WoodType.WARPED) {
                    this.setBlockState(axis.ordinal());
                }
            } else {
                // stripped to unstripped
                switch (this.getWoodType()) {
                    case OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK
                            -> this.setBlockState(3 + (this.getWoodType().ordinal() * 3) + axis.ordinal());
                }
            }
        }
    }

    public PillarAxis getPillarAxis() {
        int axisIndex = this.getBlockState() < 3 ? this.getBlockState() : (this.getBlockState() - 3 - (this.getWoodType().ordinal() * 3));
        return PillarAxis.values()[axisIndex];
    }

    public void setPillarAxis(PillarAxis pillarAxis) {
        if (this.isStripped() || (this.getWoodType() == WoodType.CRIMSON || this.getWoodType() == WoodType.WARPED)) {
            this.setBlockState(pillarAxis.ordinal());
        } else {
            this.setBlockState(3 + (this.getWoodType().ordinal() * 3) + pillarAxis.ordinal());
        }
    }

    @Override
    public String getBlockId() {
        if (this.isStripped()) {
            return switch (this.getWoodType()) {
                case OAK -> BlockID.STRIPPED_OAK_LOG;
                case SPRUCE -> BlockID.STRIPPED_SPRUCE_LOG;
                case BIRCH -> BlockID.STRIPPED_BIRCH_LOG;
                case JUNGLE -> BlockID.STRIPPED_JUNGLE_LOG;
                case ACACIA -> BlockID.STRIPPED_ACACIA_LOG;
                case DARK_OAK -> BlockID.STRIPPED_DARK_OAK_LOG;
                case CRIMSON -> BlockID.STRIPPED_CRIMSON_STEM;
                case WARPED -> BlockID.STRIPPED_WARPED_STEM;
            };
        } else {
            return switch (this.getWoodType()) {
                case OAK, SPRUCE, BIRCH, JUNGLE -> BlockID.LOG;
                case ACACIA, DARK_OAK -> BlockID.LOG2;
                case CRIMSON -> BlockID.CRIMSON_STEM;
                case WARPED -> BlockID.WARPED_STEM;
            };
        }
    }

    @Override
    public String getName() {
        return switch (this.getWoodType()) {
            case CRIMSON, WARPED -> this.getWoodType().getName() + " Stem";
            default -> this.getWoodType().getName() + " Log";
        };
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return Collections.unmodifiableList(BLOCK_STATES);
    }

    @Override
    public void setBlockState(int index) {
        // Prevent the block state from going outside its range since Mojang has different block states for different logs.
        if (!this.isStripped()) {
            switch (this.getWoodType()) {
                case OAK:
                case SPRUCE:
                case BIRCH:
                case JUNGLE:
                    if (index < 3 || index >= 15) {
                        this.setBlockState(3 + this.getWoodType().ordinal() * 3 + (index % 3));
                        return;
                    }
                    this.woodType = WoodType.values()[(int) Math.floor((index - 3) / 3d)];
                    break;
                case ACACIA:
                case DARK_OAK:
                    if (index < 15 || index >= 24) {
                        this.setBlockState(3 + this.getWoodType().ordinal() * 3 + (index % 3));
                        return;
                    }
                    this.woodType = WoodType.values()[(int) Math.floor((index - 3) / 3d)];
                    break;
                case CRIMSON:
                case WARPED:
                    if (index < 0 || index >= 3) {
                        this.setBlockState(index % 3);
                        return;
                    }
                    break;
            }
        } else {
            super.setBlockState(Math.max(0, Math.min(2, index)));
            return;
        }
        super.setBlockState(index);
    }

    @Override
    public float getHardness() {
        return 2;
    }

    @Override
    public float getBlastResistance() {
        return 2;
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
    public int getBurnOdds() {
        return 5;
    }

    @Override
    public int getFlameOdds() {
        return 5;
    }

    @Override
    public int getStackMeta() {
        if (this.isStripped()) {
            return 0;
        }

        return switch (this.getWoodType()) {
            case OAK, SPRUCE, BIRCH, JUNGLE -> this.getWoodType().ordinal();
            case ACACIA, DARK_OAK -> this.getWoodType().ordinal() - 4;
            default -> 0;
        };
    }

    @Override
    public void updateFromStackMeta(int meta) {
        switch (this.getBlockId()) {
            case BlockID.LOG:
                this.setWoodType(WoodType.values()[Math.max(0, Math.min(meta, 3))]);
                break;
            case BlockID.LOG2:
                this.setWoodType(WoodType.values()[Math.max(0, Math.min(meta, 1)) + 4]);
                break;
        }
    }

}
