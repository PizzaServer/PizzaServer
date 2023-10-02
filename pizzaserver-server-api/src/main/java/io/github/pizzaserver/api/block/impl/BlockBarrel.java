package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.OpenStatus;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockBarrel extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .putByte("open_bit", (byte) 0)
                        .build());

                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .putByte("open_bit", (byte) 1)
                        .build());
            }
        }
    });


    public BlockBarrel() {
        this(Direction.UP);
    }

    public BlockBarrel(OpenStatus status) {
        this(Direction.UP, status);
    }

    public BlockBarrel(Direction direction) {
        this(direction, OpenStatus.CLOSED);
    }

    public BlockBarrel(Direction direction, OpenStatus openStatus) {
        this.setDirection(direction);
        this.setOpen(openStatus == OpenStatus.OPEN);
    }

    public Direction getDirection() {
        return Direction.values()[this.getBlockState() / 2];
    }

    public void setDirection(Direction direction) {
        this.setBlockState(direction.ordinal() * 2 + (this.isOpen() ? 1 : 0));
    }

    public boolean isOpen() {
        return this.getBlockState() % 2 == 1;
    }

    public void setOpen(boolean open) {
        this.setBlockState(this.getDirection().ordinal() * 2 + (open ? 1 : 0));
    }

    @Override
    public String getBlockId() {
        return BlockID.BARREL;
    }

    @Override
    public String getName() {
        return "Barrel";
    }

    @Override
    public float getHardness() {
        return 2.5f;
    }

    @Override
    public float getBlastResistance() {
        return 2.5f;
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
    public int getFuelTicks() {
        return 300;
    }

}
