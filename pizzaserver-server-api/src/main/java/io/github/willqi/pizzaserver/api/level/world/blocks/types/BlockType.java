package io.github.willqi.pizzaserver.api.level.world.blocks.types;

import com.google.common.collect.BiMap;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.data.ToolType;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockLoot;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.data.PushResponse;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.BoundingBox;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.List;
import java.util.Set;

public interface BlockType {

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
     * Create a {@link Block} with this block type.
     * @return {@link Block}
     */
    Block create();

    /**
     * Create a {@link Block} with this block type and a block state index.
     * @param blockStateIndex block state index of the block
     * @return {@link Block}
     */
    Block create(int blockStateIndex);

    /**
     * Retrieve the {@link NBTCompound} for each of the block's states and the index associated with it.
     * The {@link NBTCompound}s returned should match the data in block_states.nbt in order to work correctly.
     * The index is used for getBlockStateIndex in order to determine the current state of the block.
     * (e.g. the direction of stairs)
     * Each {@link NBTCompound} must be named "states" for proper serialization/deserialization
     *
     * @return block states
     */
    BiMap<NBTCompound, Integer> getBlockStates();

    NBTCompound getBlockState(int index);

    int getBlockStateIndex(NBTCompound compound);

    BoundingBox getBoundingBox(int index);

    List<ItemStack> getDrops(int index);

    /**
     * The amount of light this block will absorb.
     * @return the amount of light absorbed by this block
     */
    int getLightAbsorption();

    /**
     * The amount of light this block will emit.
     * @return a decimal in the range of [0, 1]
     */
    float getLightEmission();

    /**
     * What this block does when pushed. (e.g. a piston)
     * @return push response
     */
    PushResponse getPushResponse();

    /**
     * If a player head is in this block, should their oxygen start depleting.
     * @return if the block has oxygen
     */
    boolean hasOxygen();

    /**
     * If the block type is solid.
     * @return if the block type is solid
     */
    boolean isSolid();

    /**
     * How strong this block is to mine.
     * @return strength of the block
     */
    float getToughness();

    /**
     * Get the origin position of the block.
     * @return the block origin
     */
    float[] getOrigin();

    /**
     * Retrieve the height of this BlockType.
     * @return height
     */
    float getHeight();

    /**
     * Retrieve the width of this BlockType.
     * @return width
     */
    float getWidth();

    /**
     * Retrieve the length of this BlockType.
     * @return length
     */
    float getLength();

    /**
     * How hard this block is to explode.
     * @return strength of the block to explode
     */
    float getBlastResistance();

    /**
     * How likely the block will be destroyed by flames when on fire.
     * @return chance of being destroyed
     */
    int getBurnOdds();

    /**
     * How likely the block will catch flame when next to a fire.
     * @return Chance of catching fire
     */
    int getFlameOdds();

    /**
     * Retrieve the friction entities should receive on this block.
     * MUST be within the range 0-1
     * @return friction of this block type
     */
    float getFriction();

    /**
     * Retrieve the geometry to use for this block type.
     * If null is returned then it will use the default block geometry
     * @return block geometry id
     */
    String getGeometry();

    /**
     * Retrieve the map colour this block displays on a map.
     * If null is returned then it will use the default map colour
     * @return hex value of the color to display on the map
     */
    String getMapColour();

    /**
     * Retrieve the rotation of this block.
     * @return rotation of the block
     */
    float[] getRotation();

    /**
     * If this block should fall if there's no block supporting it.
     * @return if the block should fall with no supports
     */
    boolean hasGravity();

    /**
     * The percentage of fall damage that should be removed when gets damaged by fall damage while falling onto this block.
     * @return a float between 0-1 that describes the ignored fall damage percent
     */
    float getFallDamageReduction();

    /**
     * Retrieve the item tool types that should be used against this block
     * in order for the item drops to spawn when the block is broken.
     * @return tools that will result in a block drop
     */
    Set<ToolType> getCorrectTools();

    /**
     * Retrieve the item tool types that are the most effective against this block.
     * @return tools that are very effective against this block
     */
    Set<ToolType> getBestTools();

    /**
     * Retrieve the loot that should be dropped when this block is broken.
     * @return set of the item loot to consider when dropping loot
     */
    Set<BlockLoot> getLoot(Player player);

    /**
     * Retrieve the blocks that this block can be placed on.
     * If empty, this block can be placed on any block
     * @return all the block types that this block can be placed on
     */
    Set<BaseBlockType> getPlaceableOnlyOn();

    /**
     * Retrieve the block to replace blocks of this block type with when mined.
     * @return the block to replace this block with after it is mined.
     */
    Block getResultBlock();

    /**
     * Called when the right click button is used against this block.
     * @param player the player who interacted with the block
     * @param block the block interacted with
     * @return if the item used to interact with this block should be called
     */
    boolean onInteract(Player player, Block block);

    /**
     * Called every tick when a player is walking on this block type.
     * @param player the player walking on the block
     * @param block the block being walked on
     */
    void onWalkedOn(Player player, Block block);

    /**
     * Called whenever a block next to this block is modified.
     * @param player the player who caused the update
     * @param block the block being walked on
     */
    void onUpdate(Player player, Block block);
    
}
