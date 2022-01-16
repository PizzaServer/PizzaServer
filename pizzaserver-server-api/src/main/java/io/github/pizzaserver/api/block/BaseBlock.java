package io.github.pizzaserver.api.block;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.PushResponse;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseBlock implements Block {

    private int blockState = 0;

    private World world;
    private int x;
    private int y;
    private int z;
    private int layer;

    public BlockLocation getLocation() {
        return new BlockLocation(this.world, this.x, this.y, this.z, this.layer);
    }

    @Override
    public void setLocation(World world, int x, int y, int z, int layer) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.layer = layer;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public int getLayer() {
        return this.layer;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public Block getSide(BlockFace blockFace) {
        BlockLocation location = this.getLocation();
        return this.getWorld().getBlock(location.toVector3i().add(blockFace.getOffset()));
    }

    @Override
    public int getBlockState() {
        return this.blockState;
    }

    @Override
    public void setBlockState(int index) {
        if (index < 0 || index >= this.getNBTStates().size()) {
            throw new IndexOutOfBoundsException("The block state index is out of bounds.");
        }

        this.blockState = index;
    }

    @Override
    public void setBlockState(NbtMap state) {
        int index = this.getNBTStates().indexOf(state);
        if (index == -1) {
            throw new NullPointerException("The provided block state does not exist on this block.");
        }

        this.setBlockState(index);
    }

    @Override
    public NbtMap getNBTState() {
        if (this.getNBTStates().size() == 0) {
            return NbtMap.EMPTY;
        }
        return this.getNBTStates().get(this.blockState);
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return Collections.singletonList(NbtMap.EMPTY);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 1, 1)).translate(this.getLocation().toVector3f());
    }

    @Override
    public String getMapColor() {
        return "";
    }

    @Override
    public Optional<String> getGeometry() {
        return Optional.empty();
    }

    @Override
    public float getBlastResistance() {
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
    public float getFallDamageReduction() {
        return 0;
    }

    @Override
    public float getFriction() {
        return 0.6f;
    }

    @Override
    public int getLightAbsorption() {
        return 0;
    }

    @Override
    public int getLightEmission() {
        return 0;
    }

    @Override
    public PushResponse getPushResponse() {
        return PushResponse.ALLOW;
    }

    @Override
    public boolean isAffectedByGravity() {
        return false;
    }

    @Override
    public boolean hasOxygen() {
        return true;
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
    public boolean isTransparent() {
        return false;
    }

    @Override
    public ItemBlock toStack() {
        return (ItemBlock) ItemRegistry.getInstance().getItem(this.getBlockId(), 1);
    }

    /**
     * This will not clone the location of the block.
     * @return block
     */
    @Override
    public Block clone() {
        try {
            Block block = (Block) super.clone();
            block.setLocation(null, 0, 0, 0, 0);

            return block;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError("Clone failed.", exception);
        }
    }

    @Override
    public int hashCode() {
        return (93 * this.blockState) + (93 * this.getBlockId().hashCode()) + (93 * this.getLocation().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Block block) {
            return block.getBlockId().equals(this.getBlockId()) && block.getBlockState() == this.getBlockState()
                    && block.getLocation().equals(this.getLocation());
        }
        return false;
    }
}
