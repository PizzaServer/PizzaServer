package io.github.willqi.pizzaserver.server.world.blocks.types;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.commons.utils.BoundingBox;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTString;
import io.github.willqi.pizzaserver.server.item.ItemToolType;

import java.util.*;

public class BlockTypeStone extends BlockTypeFullSolid {

    private static final BoundingBox HIT_BOX = new BoundingBox(new Vector3(0, 0, 0), new Vector3(1, 1, 1));

    private static final HashBiMap<NBTCompound, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NBTCompound, Integer>(){
        {
            List<String> stoneTypes = Arrays.asList("stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth");
            int stateIndex = 0;
            for (String stoneType : stoneTypes) {
                NBTCompound state = new NBTCompound();
                state.put("stone_type", new NBTString(stoneType));
                this.put(state, stateIndex++);
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.STONE;
    }

    @Override
    public String getName() {
        return "Stone";
    }

    @Override
    public BiMap<NBTCompound, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public BoundingBox getHitBox() {
        return HIT_BOX;
    }

    @Override
    public Set<ItemToolType> getCorrectTools() {
        return Collections.singleton(ItemToolType.WOOD_PICKAXE);
    }

}