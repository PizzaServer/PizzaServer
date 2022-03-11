package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.ActiveStatus;
import io.github.pizzaserver.api.block.data.BellAttachmentType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.HorizontalDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockBell extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            String[] attachmentTypes = new String[]{ "standing", "hanging", "side", "multiple" };

            for (String attachmentType : attachmentTypes) {
                for (int direction = 0; direction < 4; direction++) {
                    this.add(NbtMap.builder()
                            .putString("attachment", attachmentType)
                            .putInt("direction", direction)
                            .putByte("toggle_bit", (byte) 0)
                            .build());
                    this.add(NbtMap.builder()
                            .putString("attachment", attachmentType)
                            .putInt("direction", direction)
                            .putByte("toggle_bit", (byte) 1)
                            .build());
                }
            }
        }
    };


    public BlockBell() {
        this(HorizontalDirection.NORTH);
    }

    public BlockBell(HorizontalDirection direction) {
        this(direction, BellAttachmentType.HANGING);
    }

    public BlockBell(BellAttachmentType attachmentType) {
        this(HorizontalDirection.NORTH, attachmentType);
    }

    public BlockBell(ActiveStatus activeStatus) {
        this(HorizontalDirection.NORTH, BellAttachmentType.HANGING, activeStatus);
    }

    public BlockBell(HorizontalDirection direction, BellAttachmentType attachmentType) {
        this(direction, attachmentType, ActiveStatus.INACTIVE);
    }

    public BlockBell(HorizontalDirection direction, BellAttachmentType attachmentType, ActiveStatus activeStatus) {
        this.setDirection(direction);
        this.setAttachmentType(attachmentType);
        this.setRinging(activeStatus == ActiveStatus.ACTIVE);
    }

    public HorizontalDirection getDirection() {
        return HorizontalDirection.fromBlockStateIndex((this.getBlockState() % 8 - (this.isRinging() ? 1 : 0)) / 2);
    }

    public void setDirection(HorizontalDirection direction) {
        this.setBlockState((this.getAttachmentType().ordinal() * 8)
                + (direction.getBlockStateIndex() * 2)
                + (this.isRinging() ? 1 : 0));
    }

    public BellAttachmentType getAttachmentType() {
        return BellAttachmentType.values()[this.getBlockState() / 8];
    }

    public void setAttachmentType(BellAttachmentType attachmentType) {
        this.setBlockState((attachmentType.ordinal() * 8)
                + (this.getDirection().getBlockStateIndex() * 2)
                + (this.isRinging() ? 1 : 0));
    }

    public void setRinging(boolean ringing) {
        this.setBlockState((this.getAttachmentType().ordinal() * 8)
                + (this.getDirection().getBlockStateIndex() * 2)
                + (ringing ? 1 : 0));
    }

    public boolean isRinging() {
        return this.getBlockState() % 2 == 1;
    }

    @Override
    public String getBlockId() {
        return BlockID.BELL;
    }

    @Override
    public String getName() {
        return "Bell";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 5;
    }

    @Override
    public float getHardness() {
        return 5;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

}
