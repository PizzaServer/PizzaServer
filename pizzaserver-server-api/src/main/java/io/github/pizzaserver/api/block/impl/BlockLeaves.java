package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LeaveType;
import io.github.pizzaserver.api.block.descriptors.Flammable;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

import java.util.*;

public class BlockLeaves extends Block implements Flammable {

    protected LeaveType leaveType;

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            Arrays.asList("oak", "spruce", "birch", "jungle").forEach(leaveType -> {
                this.add(NbtMap.builder()
                        .putString("old_leaf_type", leaveType)
                        .putByte("persistent_bit", (byte) 0)
                        .putByte("update_bit", (byte) 0)
                        .build());
                this.add(NbtMap.builder()
                        .putString("old_leaf_type", leaveType)
                        .putByte("persistent_bit", (byte) 0)
                        .putByte("update_bit", (byte) 1)
                        .build());
                this.add(NbtMap.builder()
                        .putString("old_leaf_type", leaveType)
                        .putByte("persistent_bit", (byte) 1)
                        .putByte("update_bit", (byte) 0)
                        .build());
                this.add(NbtMap.builder()
                        .putString("old_leaf_type", leaveType)
                        .putByte("persistent_bit", (byte) 1)
                        .putByte("update_bit", (byte) 1)
                        .build());
            });
        }
    };

    public BlockLeaves() { this(LeaveType.OAK); }

    public BlockLeaves(LeaveType leaveType) { this.setLeaveType(leaveType); }

    public void setLeaveType(LeaveType leaveType){
        this.leaveType = leaveType;
    }

    public LeaveType getLeaveType(){ return this.leaveType; }

    @Override
    public String getBlockId() { return BlockID.LEAVES; }

    @Override
    public String getName() {
        return this.getLeaveType().getName() + " Leaves";
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
            return Collections.singleton(this.toStack());
        }
        return Collections.emptySet();
    }
}
