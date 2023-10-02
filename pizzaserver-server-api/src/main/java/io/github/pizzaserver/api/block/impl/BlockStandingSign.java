package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.StandingSignDirection;
import io.github.pizzaserver.api.block.data.WoodType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockStandingSign extends BlockSign {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            for (int i = 0; i < 16; i++) {
                this.add(NbtMap.builder()
                        .putInt("ground_sign_direction", i)
                        .build());
            }
        }
    });


    public BlockStandingSign() {
        this(WoodType.OAK);
    }

    public BlockStandingSign(WoodType woodType) {
        this(woodType, StandingSignDirection.SOUTH);
    }

    public BlockStandingSign(WoodType woodType, StandingSignDirection direction) {
        super(woodType);
        this.setDirection(direction);
    }

    @Override
    public String getBlockId() {
        return switch (this.getWoodType()) {
            case OAK -> BlockID.OAK_STANDING_SIGN;
            case SPRUCE -> BlockID.SPRUCE_STANDING_SIGN;
            case BIRCH -> BlockID.BIRCH_STANDING_SIGN;
            case JUNGLE -> BlockID.JUNGLE_STANDING_SIGN;
            case ACACIA -> BlockID.ACACIA_STANDING_SIGN;
            case DARK_OAK -> BlockID.DARK_OAK_STANDING_SIGN;
            case CRIMSON -> BlockID.CRIMSON_STANDING_SIGN;
            case WARPED -> BlockID.WARPED_STANDING_SIGN;
        };
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    public StandingSignDirection getDirection() {
        return StandingSignDirection.values()[this.getBlockState()];
    }

    public void setDirection(StandingSignDirection direction) {
        this.setBlockState(direction.ordinal());
    }

}
