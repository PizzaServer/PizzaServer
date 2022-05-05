package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.trait.HorizontalDirectionalTrait;
import io.github.pizzaserver.api.block.trait.LitTrait;
import io.github.pizzaserver.api.blockentity.type.BlockEntityCampfire;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.BoundingBox;
import io.github.pizzaserver.api.utils.HorizontalDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockCampfire extends BlockBlockEntity<BlockEntityCampfire> implements HorizontalDirectionalTrait, LitTrait {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int direction = 0; direction < 4; direction++) {
                this.add(NbtMap.builder()
                        .putInt("direction", direction)
                        .putByte("extinguished", (byte) 0)
                        .build());
                this.add(NbtMap.builder()
                        .putInt("direction", direction)
                        .putByte("extinguished", (byte) 1)
                        .build());
            }
        }
    };

    @Override
    public String getBlockId() {
        return BlockID.CAMPFIRE;
    }

    @Override
    public String getName() {
        return "Campfire";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 2f;
    }

    @Override
    public float getHardness() {
        return 2;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 0.43750f, 1))
                .translate(this.getLocation().toVector3f());
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
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
        return ToolType.AXE;
    }

    @Override
    public HorizontalDirection getDirection() {
        return HorizontalDirection.fromBlockStateIndex(this.getBlockState() / 2);
    }

    @Override
    public void setDirection(HorizontalDirection direction) {
        this.setBlockState((this.getBlockState() % 2) + (direction.getBlockStateIndex() * 2));
    }

    @Override
    public boolean isLit() {
        return this.getBlockState() % 2 == 0;
    }

    @Override
    public void setLit(boolean status) {
        this.setBlockState((this.getDirection().getBlockStateIndex() * 2) + (status ? 0 : 1));
    }

}
