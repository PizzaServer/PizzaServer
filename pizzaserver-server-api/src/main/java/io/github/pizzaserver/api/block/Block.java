package io.github.pizzaserver.api.block;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.PushResponse;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
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
import java.util.Set;

public abstract class Block implements Cloneable {

    private int blockState = 0;

    private World world;
    private int x;
    private int y;
    private int z;
    private int layer;


    /**
     * Namespace id of the block.
     * e.g. minecraft:stone
     * @return the namespace id
     */
    public abstract String getBlockId();

    /**
     * Display name of the block.
     * e.g. Stone
     * @return the display name
     */
    public abstract String getName();

    /**
     * Returning -1 means the block cannot be destroyed.
     * @return hardness of the block
     */
    public abstract float getHardness();

    public BlockLocation getLocation() {
        return new BlockLocation(this.world, this.x, this.y, this.z, this.layer);
    }

    public void setLocation(BlockLocation location) {
        this.setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getLayer());
    }

    public void setLocation(World world, Vector3i position, int layer) {
        this.setLocation(world, position.getX(), position.getY(), position.getZ(), layer);
    }

    public void setLocation(World world, int x, int y, int z, int layer) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.layer = layer;
    }

    public World getWorld() {
        return this.world;
    }

    public int getLayer() {
        return this.layer;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Block getSide(BlockFace face) {
        BlockLocation location = this.getLocation();
        return this.getWorld().getBlock(location.toVector3i().add(face.getOffset()));
    }

    public int getBlockState() {
        return this.blockState;
    }

    public void setBlockState(int index) {
        if (index < 0 || index >= this.getNBTStates().size()) {
            throw new IndexOutOfBoundsException("The block state index is out of bounds.");
        }

        this.blockState = index;
    }

    public void setBlockState(NbtMap state) {
        int index = this.getNBTStates().indexOf(state);
        if (index == -1) {
            throw new NullPointerException("The provided block state does not exist on this block.");
        }

        this.setBlockState(index);
    }

    public NbtMap getNBTState() {
        if (this.getNBTStates().size() == 0) {
            return NbtMap.EMPTY;
        }
        return this.getNBTStates().get(this.blockState);
    }

    public List<NbtMap> getNBTStates() {
        return Collections.singletonList(NbtMap.EMPTY);
    }

    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.ZERO, Vector3f.from(1, 1, 1))
                .translate(this.getLocation().toVector3f());
    }

    /**
     * Retrieve the map colour this block displays on a map.
     * If null is returned then it will use the default map colour
     * @return hex value of the color to display on the map
     */
    public String getMapColor() {
        return "";
    }

    /**
     * Retrieve the geometry to use for this block type.
     * If no geometry is returned then it will use the default block geometry
     * @return block geometry id
     */
    public Optional<String> getGeometry() {
        return Optional.empty();
    }

    /**
     * If this block can be mined efficiently without using a tool.
     * @return if it can
     */
    public boolean canBeMinedWithHand() {
        return false;
    }

    /**
     * Retrieve the tool type required to efficiently mine this block.
     * @return tool type
     */
    public ToolType getToolTypeRequired() {
        return ToolType.NONE;
    }

    /**
     * Retrieve the tool tier required to maximize efficiency while mining this block.
     * @return tool tier
     */
    public ToolTier getToolTierRequired() {
        return ToolTier.NONE;
    }

    /**
     * Returning -1 means the block cannot explode.
     * @return blast resistance
     */
    public float getBlastResistance() {
        return 0;
    }

    /**
     * The percentage of fall damage that should be removed when gets damaged by fall damage while falling onto this block.
     * @return a float between 0-1 that describes the ignored fall damage percent
     */
    public float getFallDamageReduction() {
        return 0;
    }

    /**
     * Retrieve the friction entities should receive on this block.
     * MUST be within the range 0-1
     * @return friction of this block
     */
    public float getFriction() {
        return 0.6f;
    }

    public int getLightAbsorption() {
        return 0;
    }

    public int getLightEmission() {
        return 0;
    }

    public PushResponse getPushResponse() {
        return PushResponse.ALLOW;
    }

    public boolean isAffectedByGravity() {
        return false;
    }

    public boolean hasOxygen() {
        return true;
    }

    /**
     * If true, entities cannot pass through this block.
     * @return if this block allows entities to walk through it
     */
    public boolean hasCollision() {
        return true;
    }

    /**
     * If true, when a player attempts to place a block at the position of this block,
     * the block at this location will be replaced.
     */
    public boolean isReplaceable() {
        return false;
    }

    /**
     * If true, light can pass through this block.
     * @return if light can pass through
     */
    public boolean isTransparent() {
        return false;
    }

    public ItemBlock toStack() {
        return new ItemBlock(this.getBlockId(), 1);
    }

    public BlockBehavior getBehavior() {
        return BlockRegistry.getInstance().getBlockBehavior(this);
    }

    /**
     * Get the items that should drop when this block is mined with the correct tool and tier.
     * @param entity the entity that mined this block
     * @return the drops
     */
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(this.toStack());
    }

    public boolean isAir() {
        return BlockID.AIR.equals(this.getBlockId());
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
        return (93 * this.blockState)
                + (93 * this.getBlockId().hashCode())
                + (93 * this.getLocation().hashCode());
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