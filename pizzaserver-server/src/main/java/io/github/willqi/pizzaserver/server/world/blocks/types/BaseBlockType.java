package io.github.willqi.pizzaserver.server.world.blocks.types;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.world.blocks.Block;
import io.github.willqi.pizzaserver.api.world.blocks.BlockLoot;
import io.github.willqi.pizzaserver.api.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.api.world.blocks.types.data.PushResponse;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.api.item.ItemToolType;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.world.blocks.ImplBlock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public abstract class BaseBlockType implements BlockType {

    private static final HashBiMap<NBTCompound, Integer> EMPTY_BLOCK_STATES = HashBiMap.create(new HashMap<NBTCompound, Integer>(){
        {
            this.put(new NBTCompound("states"), 0);
        }
    });


    @Override
    public Block create() {
        return new ImplBlock(this);
    }

    @Override
    public Block create(int blockStateIndex) {
        Block block = new ImplBlock(this);
        block.setBlockStateIndex(blockStateIndex);
        return block;
    }

    @Override
    public BiMap<NBTCompound, Integer> getBlockStates() {
        return EMPTY_BLOCK_STATES;
    }

    @Override
    public NBTCompound getBlockState(int index) {
        return this.getBlockStates().inverse().getOrDefault(index, null);
    }

    @Override
    public int getBlockStateIndex(NBTCompound compound) {
        return this.getBlockStates().getOrDefault(compound, -1);
    }

    @Override
    public int getLightAbsorption() {
        return 0;
    }

    @Override
    public float getLightEmission() {
        return 0;
    }

    @Override
    public PushResponse getPushResponse() {
        return PushResponse.ALLOW;
    }

    @Override
    public boolean hasOxygen() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public int getToughness() {
        return 0;
    }

    @Override
    public float[] getOrigin() {
        return new float[]{ -8f, 0f, -8f };
    }

    @Override
    public float getHeight() {
        return 16f;
    }

    @Override
    public float getWidth() {
        return 16f;
    }

    @Override
    public float getLength() {
        return 16f;
    }

    @Override
    public float getBlastResistance() {
        return 0;
    }

    @Override
    public int getBurnOdds() {
        return 0;
    }

    @Override
    public int getFlameOdds() {
        return 0;
    }

    @Override
    public float getFriction() {
        return 0.1f;
    }

    @Override
    public String getGeometry() {
        return null;
    }

    @Override
    public String getMapColour() {
        return null;
    }

    @Override
    public boolean allowsJumping() {
        return true;
    }

    @Override
    public int[] getRotation() {
        return new int[]{ 0, 0, 0 };
    }

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public float getFallDamageReduction() {
        return 0;
    }

    @Override
    public Set<ItemToolType> getCorrectTools() {
        return Collections.singleton(ItemToolType.ANY);
    }

    @Override
    public Set<BlockLoot> getLoot(Player player) {
        return Collections.emptySet();
    }

    @Override
    public void onInteract(Player player, Block block) {}

    @Override
    public void onWalkedOn(Player player, Block block) {}

    @Override
    public void onUpdate(Player player, Block block) {}


    /**
     * Retrieve a block type based on it's block id
     * @param blockId id of the block (e.g. minecraft:stone)
     * @return {@link BlockType} of the id
     */
    public static BlockType getBlockTypeById(String blockId) {
        return ImplServer.getInstance().getBlockRegistry().getBlockType(blockId);
    }


}
