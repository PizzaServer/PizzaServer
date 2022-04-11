package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.traits.HorizontalDirectionalTrait;
import io.github.pizzaserver.api.utils.Direction;
import io.github.pizzaserver.api.utils.HorizontalDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockWallSign extends BlockSign implements HorizontalDirectionalTrait {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .build());
            }
        }
    });


    public BlockWallSign() {
        this(WoodType.OAK);
    }

    public BlockWallSign(WoodType woodType) {
        this(woodType, HorizontalDirection.SOUTH);
    }

    public BlockWallSign(WoodType woodType, HorizontalDirection direction) {
        super(woodType);
        this.setDirection(direction);
    }

    @Override
    public String getBlockId() {
        return switch (this.getWoodType()) {
            case OAK -> BlockID.OAK_WALL_SIGN;
            case SPRUCE -> BlockID.SPRUCE_WALL_SIGN;
            case BIRCH -> BlockID.BIRCH_WALL_SIGN;
            case JUNGLE -> BlockID.JUNGLE_WALL_SIGN;
            case ACACIA -> BlockID.ACACIA_WALL_SIGN;
            case DARK_OAK -> BlockID.DARK_OAK_WALL_SIGN;
            case CRIMSON -> BlockID.CRIMSON_WALL_SIGN;
            case WARPED -> BlockID.WARPED_WALL_SIGN;
        };
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public HorizontalDirection getDirection() {
        return Direction.fromBlockStateIndex(this.getBlockState()).toHorizontal();
    }

    @Override
    public void setDirection(HorizontalDirection direction) {
        this.setBlockState(direction.getOmniBlockStateIndex());
    }

}
