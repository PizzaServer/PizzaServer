package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.BorderAttachmentType;
import io.github.pizzaserver.api.block.data.PushResponse;
import io.github.pizzaserver.api.utils.BoundingBox;
import io.github.pizzaserver.api.utils.HorizontalDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockBorder extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = Collections.unmodifiableList(new ArrayList<>() {
        {
            String[] wallConnectionTypes = new String[] { "none", "tall", "short" };
            for (String eastConnectionType : wallConnectionTypes) {
                for (String northConnectionType : wallConnectionTypes) {
                    for (String southConnectionType : wallConnectionTypes) {
                        for (String westConnectionType : wallConnectionTypes) {
                            this.add(NbtMap.builder()
                                    .putString("wall_connection_type_east", eastConnectionType)
                                    .putString("wall_connection_type_north", northConnectionType)
                                    .putString("wall_connection_type_south", southConnectionType)
                                    .putString("wall_connection_type_west", westConnectionType)
                                    .putBoolean("wall_post_bit", false)
                                    .build());

                            this.add(NbtMap.builder()
                                    .putString("wall_connection_type_east", eastConnectionType)
                                    .putString("wall_connection_type_north", northConnectionType)
                                    .putString("wall_connection_type_south", southConnectionType)
                                    .putString("wall_connection_type_west", westConnectionType)
                                    .putBoolean("wall_post_bit", true)
                                    .build());
                        }
                    }
                }
            }
        }
    });

    @Override
    public String getBlockId() {
        return BlockID.BORDER_BLOCK;
    }

    @Override
    public String getName() {
        return "Border";
    }

    @Override
    public float getHardness() {
        return -1;
    }

    @Override
    public float getBlastResistance() {
        return -1;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public PushResponse getPushResponse() {
        return PushResponse.DENY;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 1.5f, 1))
                .translate(this.getLocation().toVector3f());
    }

    public BorderAttachmentType getAttachmentType(HorizontalDirection direction) {
        return switch (direction) {
            case EAST -> BorderAttachmentType.values()[this.getBlockState() / 54];
            case NORTH -> BorderAttachmentType.values()[(this.getBlockState() % 54) / 18];
            case SOUTH -> BorderAttachmentType.values()[(this.getBlockState() % 54 % 18) / 6];
            case WEST -> BorderAttachmentType.values()[(this.getBlockState() % 54 % 18 % 6) / 2];
        };
    }

    public void setAttachmentType(HorizontalDirection direction, BorderAttachmentType attachmentType) {
        switch (direction) {
            case EAST -> this.setBlockState((this.getBlockState() % 54) + attachmentType.ordinal() * 54);
            case NORTH -> this.setBlockState((this.getBlockState() % 54 % 18)
                    + (attachmentType.ordinal() * 18)
                    + (this.getAttachmentType(HorizontalDirection.EAST).ordinal() * 54));
            case SOUTH -> this.setBlockState((this.getBlockState() % 54 % 18 % 6)
                    + (attachmentType.ordinal() * 6)
                    + (this.getAttachmentType(HorizontalDirection.EAST).ordinal() * 54)
                    + (this.getAttachmentType(HorizontalDirection.NORTH).ordinal() * 18));
            case WEST -> this.setBlockState((this.getBlockState() % 54 % 18 % 6 % 2)
                    + (attachmentType.ordinal() * 2)
                    + (this.getAttachmentType(HorizontalDirection.EAST).ordinal() * 54)
                    + (this.getAttachmentType(HorizontalDirection.NORTH).ordinal() * 18)
                    + (this.getAttachmentType(HorizontalDirection.SOUTH).ordinal() * 6));
        }
    }

    public boolean isWallPost() {
        return this.getBlockState() % 2 == 1;
    }

    public void setWallPost(boolean enabled) {
        this.setBlockState(this.getBlockState() - (this.getBlockState() % 2) + (enabled ? 1 : 0));
    }

}
