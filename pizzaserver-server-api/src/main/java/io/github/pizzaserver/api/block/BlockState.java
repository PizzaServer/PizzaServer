package io.github.pizzaserver.api.block;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.block.types.data.PushResponse;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.Set;

public class BlockState {

    protected final BlockType blockType;
    protected final int index;

    public BlockState(BlockType blockType, int index) {
        this.blockType = blockType;
        this.index = index;
    }

    public String getBlockId() {
        return this.getBlockType().getBlockId();
    }

    public String getName() {
        return this.getBlockType().getName(this.index);
    }

    public NbtMap getNBT() {
        return this.getBlockType().getBlockState(this.index);
    }

    public BoundingBox getBoundingBox() {
        return this.getBlockType().getBoundingBox(this.index);
    }

    public int getLightAbsorption() {
        return this.getBlockType().getLightAbsorption(this.index);
    }

    public float getLightEmission() {
        return this.getBlockType().getLightEmission(this.index);
    }

    public PushResponse getPushResponse() {
        return this.getBlockType().getPushResponse(this.index);
    }

    public boolean hasOxygen() {
        return this.getBlockType().hasOxygen(this.index);
    }

    public boolean isLiquid() {
        return this.getBlockType().isLiquid(this.index);
    }

    public boolean isAir() {
        return this.getBlockId().equals(BlockTypeID.AIR);
    }

    public boolean isSolid() {
        return this.getBlockType().isSolid(this.index);
    }

    public float getHardness() {
        return this.getBlockType().getHardness(this.index);
    }

    public float[] getOrigin() {
        return this.getBlockType().getOrigin(this.index);
    }

    public float getHeight() {
        return this.getBlockType().getHeight(this.index);
    }

    public float getLength() {
        return this.getBlockType().getLength(this.index);
    }

    public float getBlastResistance() {
        return this.getBlockType().getBlastResistance(this.index);
    }

    public int getBurnOdds() {
        return this.getBlockType().getBurnOdds(this.index);
    }

    public int getFlameOdds() {
        return this.getBlockType().getFlameOdds(this.index);
    }

    public float getFriction() {
        return this.getBlockType().getFriction(this.index);
    }

    public String getGeometry() {
        return this.getBlockType().getGeometry(this.index);
    }

    public String getMapColor() {
        return this.getBlockType().getMapColor(this.index);
    }

    public float[] getRotation() {
        return this.getBlockType().getRotation(this.index);
    }

    public boolean hasGravity() {
        return this.getBlockType().hasGravity(this.index);
    }

    public float getFallDamageReduction() {
        return this.getBlockType().getFallDamageReduction(this.index);
    }

    public boolean canBeMinedWithHand() {
        return this.getBlockType().canBeMinedWithHand();
    }

    public ToolType getToolType() {
        return this.getBlockType().getToolTypeRequired();
    }

    public ToolTier getToolTier() {
        return this.getBlockType().getToolTierRequired();
    }

    public Set<ItemStack> getLoot(Player player) {
        return this.getBlockType().getLoot(player, this.index);
    }

    public Set<BlockState> getPlaceableOnlyOn() {
        return this.getBlockType().getPlaceableOnlyOn(this.index);
    }

    public BlockType getBlockType() {
        return this.blockType;
    }

    public int getIndex() {
        return this.index;
    }



}
