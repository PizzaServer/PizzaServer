package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.SoundEvent;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockFace;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.BlockUpdateType;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.item.ItemStack;

import java.util.HashMap;

public abstract class BlockTypeButton extends BaseBlockType {

    protected static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.put(NbtMap.builder()
                        .putByte("button_pressed_bit", (byte) 0)
                        .putInt("facing_direction", i)
                        .build(), i);
            }

            for (int i = 0; i < 6; i++) {
                this.put(NbtMap.builder()
                        .putByte("button_pressed_bit", (byte) 1)
                        .putInt("facing_direction", i)
                        .build(), i + 6);
            }
        }
    });

    protected final String buttonId;


    public BlockTypeButton(String buttonId) {
        this.buttonId = buttonId;
    }

    @Override
    public String getBlockId() {
        return this.buttonId;
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
    public boolean hasCollision() {
        return false;
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        block.setBlockStateIndex(face.ordinal());
        return block.getSide(face.opposite()).getBlockState().hasCollision();
    }

    @Override
    public boolean onInteract(Entity entity, Block block, BlockFace face) {
        if (block.getBlockStateIndex() < 6) {
            block.setBlockStateIndex(block.getBlockStateIndex() + 6);   // Power it on
            block.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());
            block.getWorld().playSound(SoundEvent.POWER_ON, block.getLocation().toVector3f());
            block.getWorld().requestBlockUpdate(BlockUpdateType.BLOCK, block.getLocation().toVector3i(), 30);
        }
        return true;
    }

    @Override
    public void onUpdate(BlockUpdateType type, Block block) {
        switch (type) {
            case BLOCK:
                if (block.getBlockStateIndex() >= 6) {
                    block.setBlockStateIndex(block.getBlockStateIndex() - 6);   // power it off
                    block.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());
                    block.getWorld().playSound(SoundEvent.POWER_OFF, block.getLocation().toVector3f());
                }
                break;
            case NEIGHBOUR:
                Block parentBlock = block.getSide(BlockFace.resolve(block.getBlockStateIndex()).opposite());
                if (!parentBlock.getBlockState().hasCollision()) {
                    block.getWorld().addItemEntity(new ItemStack(this.getBlockId(), 1),
                            block.getLocation().toVector3f(),
                            ItemEntity.getRandomMotion());
                    block.getWorld().setAndUpdateBlock(BlockRegistry.getInstance().getBlockType(BlockTypeID.AIR), block.getLocation().toLocation().toVector3i());
                }
                break;
        }
    }

}
