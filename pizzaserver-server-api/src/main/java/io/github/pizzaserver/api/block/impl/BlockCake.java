package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockCake extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int bite = 0; bite < 7; bite++) {
                this.add(NbtMap.builder()
                        .putInt("bite_counter", bite)
                        .build());
            }
        }
    };


    public int getBites() {
        return this.getBlockState();
    }

    public void setBites(int bites) {
        this.setBlockState(Math.max(0, Math.min(bites, 6)));
    }

    @Override
    public String getBlockId() {
        return BlockID.CAKE;
    }

    @Override
    public String getName() {
        return "Cake";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getBlastResistance() {
        return 0.5f;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.from(0.0625f + (this.getBites() / 8d), 0, 0.0625f), Vector3f.from(0.9375f, 0.5f, 0.9375f))
                .translate(this.getLocation().toVector3f());
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.emptySet();
    }

}
