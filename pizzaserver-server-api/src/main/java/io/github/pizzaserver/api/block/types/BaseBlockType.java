package io.github.pizzaserver.api.block.types;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.BlockState;
import io.github.pizzaserver.api.block.types.data.PushResponse;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.ToolTypeRegistry;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public abstract class BaseBlockType implements BlockType {

    private static final HashBiMap<NbtMap, Integer> EMPTY_BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
        {
            this.put(NbtMap.EMPTY, 0);
        }
    });

    @Override
    public Block create() {
        return new Block(this);
    }

    @Override
    public Block create(int blockStateIndex) {
        Block block = new Block(this);
        block.setBlockStateIndex(blockStateIndex);
        return block;
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return EMPTY_BLOCK_STATES;
    }

    @Override
    public NbtMap getBlockState(int index) {
        return this.getBlockStates().inverse().getOrDefault(index, null);
    }

    @Override
    public int getBlockStateIndex(NbtMap compound) {
        return this.getBlockStates().getOrDefault(compound, -1);
    }

    @Override
    public BoundingBox getBoundingBox(int blockStateIndex) {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setHeight(1f);
        boundingBox.setWidth(1f);
        return boundingBox;
    }

    @Override
    public int getLightAbsorption(int blockStateIndex) {
        return 0;
    }

    @Override
    public float getLightEmission(int blockStateIndex) {
        return 0;
    }

    @Override
    public PushResponse getPushResponse(int blockStateIndex) {
        return PushResponse.ALLOW;
    }

    @Override
    public boolean hasOxygen(int blockStateIndex) {
        return true;
    }

    @Override
    public boolean isLiquid(int blockStateIndex) {
        return false;
    }

    @Override
    public boolean isSolid(int blockStateIndex) {
        return true;
    }

    @Override
    public float[] getOrigin(int blockStateIndex) {
        return new float[]{ -8f, 0f, -8f };
    }

    @Override
    public float getHeight(int blockStateIndex) {
        return 16f;
    }

    @Override
    public float getWidth(int blockStateIndex) {
        return 16f;
    }

    @Override
    public float getLength(int blockStateIndex) {
        return 16f;
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 0;
    }

    @Override
    public int getBurnOdds(int blockStateIndex) {
        return 0;
    }

    @Override
    public int getFlameOdds(int blockStateIndex) {
        return 0;
    }

    @Override
    public float getFriction(int blockStateIndex) {
        return 0.6f;
    }

    @Override
    public String getGeometry(int blockStateIndex) {
        return null;
    }

    @Override
    public String getMapColor(int blockStateIndex) {
        return null;
    }

    @Override
    public float[] getRotation(int blockStateIndex) {
        return new float[]{ 0, 0, 0 };
    }

    @Override
    public boolean hasGravity(int blockStateIndex) {
        return false;
    }

    @Override
    public float getFallDamageReduction(int blockStateIndex) {
        return 0;
    }

    @Override
    public Set<ToolType> getCorrectTools(int blockStateIndex) {
        return Collections.singleton(ToolTypeRegistry.getToolType(ToolTypeID.NONE));
    }

    @Override
    public Set<ToolType> getBestTools(int blockStateIndex) {
        return Collections.singleton(ToolTypeRegistry.getToolType(ToolTypeID.NONE));
    }

    @Override
    public Set<ItemStack> getLoot(Player player, int blockStateIndex) {
        return Collections.singleton(ItemRegistry.getInstance().getItem(this.getBlockId(), 1, blockStateIndex));
    }

    @Override
    public Set<BlockState> getPlaceableOnlyOn(int blockStateIndex) {
        return Collections.emptySet();
    }

    @Override
    public Block getResultBlock(int blockStateIndex) {
        return BlockRegistry.getInstance().getBlock(BlockTypeID.AIR);
    }

    @Override
    public boolean onInteract(Player player, Block block) {
        return true;
    }

    @Override
    public void onWalkedOn(Entity entity, Block block) {}

    @Override
    public void onWalkedOff(Entity entity, Block block) {}

    @Override
    public void onStandingOn(Entity entity, Block block) {}

    @Override
    public void onUpdate(Block block) {}

}
