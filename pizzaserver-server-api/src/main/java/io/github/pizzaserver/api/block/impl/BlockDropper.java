package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.ActiveStatus;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockDropper extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .putByte("triggered_bit", (byte) 0)
                        .build());

                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .putByte("triggered_bit", (byte) 1)
                        .build());
            }
        }
    });


    public BlockDropper() {
        this(Direction.UP);
    }

    public BlockDropper(Direction direction) {
        this(direction, ActiveStatus.INACTIVE);
    }

    public BlockDropper(ActiveStatus activeStatus) {
        this(Direction.UP, activeStatus);
    }

    public BlockDropper(Direction direction, ActiveStatus activeStatus) {
        this.setDirection(direction);
        this.setTriggered(activeStatus == ActiveStatus.ACTIVE);
    }

    public Direction getDirection() {
        return Direction.fromBlockStateIndex(this.getBlockState() / 2);
    }

    public void setDirection(Direction direction) {
        this.setBlockState(direction.getBlockStateIndex() * 2 + (this.isTriggered() ? 1 : 0));
    }

    public boolean isTriggered() {
        return this.getBlockState() % 2 == 1;
    }

    public void setTriggered(boolean triggered) {
        this.setBlockState(this.getDirection().getBlockStateIndex() * 2 + (triggered ? 1 : 0));
    }

    @Override
    public String getBlockId() {
        return BlockID.DROPPER;
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public float getHardness() {
        return 3.5f;
    }

    @Override
    public float getBlastResistance() {
        return 3.5f;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

}
