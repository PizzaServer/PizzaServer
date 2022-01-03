package io.github.pizzaserver.api.block.types;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockFace;
import io.github.pizzaserver.api.block.BlockUpdateType;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.types.data.PushResponse;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.ItemStack;
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
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return EMPTY_BLOCK_STATES;
    }

    @Override
    public NbtMap getBlockStateNBT(int index) {
        return this.getBlockStateNBTs().inverse().getOrDefault(index, null);
    }

    @Override
    public int getBlockStateIndex(NbtMap compound) {
        return this.getBlockStateNBTs().getOrDefault(compound, -1);
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
    public PushResponse getPushResponse() {
        return PushResponse.ALLOW;
    }

    @Override
    public boolean hasOxygen() {
        return true;
    }

    @Override
    public boolean isLiquid() {
        return false;
    }

    @Override
    public boolean hasCollision() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return false;
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
    public boolean hasGravity() {
        return false;
    }

    @Override
    public float getFallDamageReduction() {
        return 0;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return false;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.NONE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.NONE;
    }

    @Override
    public Set<ItemStack> getLoot(Entity entity, int blockStateIndex) {
        return Collections.singleton(ItemRegistry.getInstance().getItem(this.getBlockId(), 1));
    }

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        return true;
    }

    @Override
    public void onPlace(Entity entity, Block block, BlockFace face) {}

    @Override
    public boolean onInteract(Entity entity, Block block, BlockFace face) {
        return true;
    }

    @Override
    public void onBreak(Entity entity, Block block) {
        for (ItemStack loot : this.getLoot(entity, block.getBlockStateIndex())) {
            block.getWorld().addItemEntity(loot,
                    block.getLocation().toVector3f().add(0.5f, 0.5f, 0.5f),
                    ItemEntity.getRandomMotion());
        }
    }

    @Override
    public void onWalkedOn(Entity entity, Block block) {}

    @Override
    public void onWalkedOff(Entity entity, Block block) {}

    @Override
    public void onStandingOn(Entity entity, Block block) {}

    @Override
    public void onUpdate(BlockUpdateType type, Block block) {}

}
