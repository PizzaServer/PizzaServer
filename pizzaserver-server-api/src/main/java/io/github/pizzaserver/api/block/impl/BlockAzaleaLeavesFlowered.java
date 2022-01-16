package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.descriptors.Flammable;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockAzaleaLeavesFlowered extends Block implements Flammable {

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
        return BlockID.AZALEA_LEAVES_FLOWERED;
    }

    @Override
    public String getName() {
        return "Azalea Leaves Flowered";
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
        if (entity.getInventory().getHeldItem() instanceof ToolItemComponent toolItemComponent
                && toolItemComponent.getToolType() == ToolType.SHEARS) {
            return Collections.singleton(this.toStack());
        }
        return Collections.emptySet();
    }

}
