package io.github.pizzaserver.api.block;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.PushResponse;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Block extends Cloneable {

    /**
     * Namespace id of the block.
     * e.g. minecraft:stone
     * @return the namespace id
     */
    String getBlockId();

    /**
     * Display name of the block.
     * e.g. Stone
     * @return the display name
     */
    String getName();

    /**
     * Returning -1 means the block cannot be destroyed.
     * @return hardness of the block
     */
    float getHardness();

    /**
     * Retrieve the id used when serializing this block into an item.
     * Some blocks have different item ids compared to their block ids.
     * (e.g. stone_slab4 is a block id, however there is no runtime item state for it. Instead, double_stone_slab4 is used)
     * @return item id
     */
    String getItemId();

    BlockLocation getLocation();

    default void setLocation(BlockLocation location) {
        this.setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getLayer());
    }

    default void setLocation(World world, Vector3i position, int layer) {
        this.setLocation(world, position.getX(), position.getY(), position.getZ(), layer);
    }

    void setLocation(World world, int x, int y, int z, int layer);

    World getWorld();

    int getLayer();

    int getX();

    int getY();

    int getZ();

    Block getSide(BlockFace face);

    int getBlockState();

    void setBlockState(int index);

    void setBlockState(NbtMap state);

    NbtMap getNBTState();

    List<NbtMap> getNBTStates();

    BoundingBox getBoundingBox();

    /**
     * Retrieve the map colour this block displays on a map.
     * If null is returned then it will use the default map colour
     * @return hex value of the color to display on the map
     */
    String getMapColor();

    /**
     * Retrieve the geometry to use for this block type.
     * If no geometry is returned then it will use the default block geometry
     * @return block geometry id
     */
    Optional<String> getGeometry();

    /**
     * If this block can be mined efficiently without using a tool.
     * @return if it can
     */
    boolean canBeMinedWithHand();

    /**
     * Retrieve the tool type required to efficiently mine this block.
     * @return tool type
     */
    ToolType getToolTypeRequired();

    /**
     * Retrieve the tool tier required to maximize efficiency while mining this block.
     * @return tool tier
     */
    ToolTier getToolTierRequired();

    /**
     * Returning -1 means the block cannot explode.
     * @return blast resistance
     */
    float getBlastResistance();

    /**
     * The percentage of fall damage that should be removed when gets damaged by fall damage while falling onto this block.
     * @return a float between 0-1 that describes the ignored fall damage percent
     */
    float getFallDamageReduction();

    /**
     * If this block can be set on fire.
     * @return if this block can be set on fire.
     */
    boolean canBeIgnited();

    /**
     * If the fire on this block should ever be extinguished.
     * @return if the fire on this block can burn forever.
     */
    boolean canBurnForever();

    /**
     * Retrieve the friction entities should receive on this block.
     * MUST be within the range 0-1
     * @return friction of this block
     */
    float getFriction();

    int getLightAbsorption();

    int getLightEmission();

    PushResponse getPushResponse();

    boolean isAffectedByGravity();

    boolean hasOxygen();

    /**
     * If true, entities cannot pass through this block.
     * @return if this block allows entities to walk through it
     */
    boolean hasCollision();

    /**
     * If true, when a player attempts to place a block at the position of this block,
     * the block at this location will be replaced.
     */
    boolean isReplaceable();

    /**
     * If true, light can pass through this block.
     * @return if light can pass through
     */
    boolean isTransparent();

    /**
     * Retrieve the meta assigned to this block when serializing as an item.
     * @return stack meta
     */
    int getStackMeta();

    /**
     * Modify this block appropriately based on the item equivalent form of this block's meta.
     * @param meta stack meta
     */
    void updateFromStackMeta(int meta);

    BlockBehavior<Block> getBehavior();

    /**
     * Get the items that should drop when this block is mined with the correct tool and tier.
     * @param entity the entity that mined this block
     * @return the drops
     */
    Set<Item> getDrops(Entity entity);

    boolean isAir();

    int getMaxStackSize();

    ItemBlock toItem();

    Block clone();

}