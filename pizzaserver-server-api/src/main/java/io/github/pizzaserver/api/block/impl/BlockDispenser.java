package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.ActiveStatus;
import io.github.pizzaserver.api.blockentity.type.BlockEntityDispenser;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockDispenser extends BlockBlockEntity<BlockEntityDispenser> {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .putBoolean("triggered_bit", false)
                        .build());

                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .putBoolean("triggered_bit", true)
                        .build());
            }
        }
    });


    public BlockDispenser() {
        this(Direction.UP);
    }

    public BlockDispenser(Direction direction) {
        this(direction, ActiveStatus.INACTIVE);
    }

    public BlockDispenser(ActiveStatus activeStatus) {
        this(Direction.UP, activeStatus);
    }

    public BlockDispenser(Direction direction, ActiveStatus activeStatus) {
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
        return BlockID.DISPENSER;
    }

    @Override
    public String getName() {
        return "Dispenser";
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
