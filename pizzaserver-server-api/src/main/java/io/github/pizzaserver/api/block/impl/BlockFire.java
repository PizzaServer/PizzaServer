package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockFire extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int age = 0; age < 16; age++) {
                this.add(NbtMap.builder()
                        .putInt("age", age)
                        .build());
            }
        }
    };


    public BlockFire() {
        this(0);
    }

    public BlockFire(int age) {
        this.setAge(age);
    }

    public int getAge() {
        return this.getBlockState();
    }

    public void setAge(int age) {
        this.setBlockState(Math.max(0, Math.min(age, 15)));
    }

    @Override
    public String getBlockId() {
        return BlockID.FIRE;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public String getName() {
        return "Fire";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public int getLightEmission() {
        return 15;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.emptySet();
    }

}
