package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.trait.FlammableTrait;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockAzaleaLeaves extends BaseBlock implements FlammableTrait {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder()
                    .putByte("persistent_bit", (byte) 0)
                    .putByte("update_bit", (byte) 0)
                    .build());
            this.add(NbtMap.builder()
                    .putByte("persistent_bit", (byte) 0)
                    .putByte("update_bit", (byte) 1)
                    .build());
            this.add(NbtMap.builder()
                    .putByte("persistent_bit", (byte) 1)
                    .putByte("update_bit", (byte) 0)
                    .build());
            this.add(NbtMap.builder()
                    .putByte("persistent_bit", (byte) 1)
                    .putByte("update_bit", (byte) 1)
                    .build());
        }
    };

    @Override
    public String getBlockId() {
        return BlockID.AZALEA_LEAVES;
    }

    @Override
    public String getName() {
        return "Azalea Leaves";
    }

    @Override
    public float getHardness() {
        return 0.2f;
    }

    @Override
    public float getBlastResistance() {
        return 0.2f;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHEARS;
    }

    @Override
    public int getBurnOdds() {
        return 30;
    }

    @Override
    public int getFlameOdds() {
        return 60;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        if (entity.getInventory().getHeldItem() instanceof ToolItem toolItemComponent
                && toolItemComponent.getToolType() == ToolType.SHEARS) {
            return Collections.singleton(this.toItem());
        }
        return Collections.emptySet();
    }

}
