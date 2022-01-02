package io.github.pizzaserver.api.block.types;

import com.google.common.collect.BiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockState;
import io.github.pizzaserver.api.block.types.data.PushResponse;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.BoundingBox;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;

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
    String getName(int blockStateIndex);

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
     * Retrieve the {@link NbtMap} for each of the block's states and the index associated with it.
     * The {@link NbtMap}s returned should match the data in block_states.nbt in order to work correctly.
     * The index is used for getBlockStateIndex in order to determine the current state of the block.
     * (e.g. the direction of stairs)
     * Each {@link NbtMap} must be named "states" for proper serialization/deserialization
     *
     * @return block states
     */
    BiMap<NbtMap, Integer> getBlockStateNBTs();

    NbtMap getBlockStateNBT(int index);

    int getBlockStateIndex(NbtMap compound);

    BoundingBox getBoundingBox(int index);

    /**
     * The amount of light this block will absorb.
     * @return the amount of light absorbed by this block
     */
    int getLightAbsorption(int blockStateIndex);

    /**
     * The amount of light this block will emit.
     * @return a decimal in the range of [0, 1]
     */
    float getLightEmission(int blockStateIndex);

    /**
     * What this block does when pushed. (e.g. a piston)
     * @return push response
     */
    PushResponse getPushResponse(int blockStateIndex);

    /**
     * If a player head is in this block, should their oxygen start depleting.
     * @return if the block has oxygen
     */
    boolean hasOxygen(int blockStateIndex);

    /**
     * If this block type is liquid and swimmable.
     * @return if the block type is a liquid and can entities can swim in this.
     */
    boolean isLiquid(int blockStateIndex);

    /**
     * If the block type is solid.
     * @return if the block type is solid
     */
    boolean isSolid(int blockStateIndex);

    /**
     * How hard this block is to mine.
     * @return strength of the block
     */
    float getHardness(int blockStateIndex);

    /**
     * Get the origin position of the block.
     * @return the block origin
     */
    float[] getOrigin(int blockStateIndex);

    /**
     * Retrieve the height of this BlockType.
     * @return height
     */
    float getHeight(int blockStateIndex);

    /**
     * Retrieve the width of this BlockType.
     * @return width
     */
    float getWidth(int blockStateIndex);

    /**
     * Retrieve the length of this BlockType.
     * @return length
     */
    float getLength(int blockStateIndex);

    /**
     * How hard this block is to explode.
     * @return strength of the block to explode
     */
    float getBlastResistance(int blockStateIndex);

    /**
     * How likely the block will be destroyed by flames when on fire.
     * @return chance of being destroyed
     */
    int getBurnOdds(int blockStateIndex);

    /**
     * How likely the block will catch flame when next to a fire.
     * @return Chance of catching fire
     */
    int getFlameOdds(int blockStateIndex);

    /**
     * Retrieve the friction entities should receive on this block.
     * MUST be within the range 0-1
     * @return friction of this block type
     */
    float getFriction(int blockStateIndex);

    /**
     * Retrieve the geometry to use for this block type.
     * If null is returned then it will use the default block geometry
     * @return block geometry id
     */
    String getGeometry(int blockStateIndex);

    /**
     * Retrieve the map colour this block displays on a map.
     * If null is returned then it will use the default map colour
     * @return hex value of the color to display on the map
     */
    String getMapColor(int blockStateIndex);

    /**
     * Retrieve the rotation of this block.
     * @return rotation of the block
     */
    float[] getRotation(int blockStateIndex);

    /**
     * If this block should fall if there's no block supporting it.
     * @return if the block should fall with no supports
     */
    boolean hasGravity(int blockStateIndex);

    /**
     * The percentage of fall damage that should be removed when gets damaged by fall damage while falling onto this block.
     * @return a float between 0-1 that describes the ignored fall damage percent
     */
    float getFallDamageReduction(int blockStateIndex);

    /**
     * If this block can be mined efficiently without using a tool.
     * @return if it does not require a tool
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
     * Retrieve the loot that should be dropped when this block is broken.
     * @return set of the item loot to consider when dropping loot
     */
    Set<ItemStack> getLoot(Entity entity, int blockStateIndex);

    /**
     * Retrieve the blocks that this block can be placed on.
     * If empty, this block can be placed on any block
     * @return all the block types that this block can be placed on
     */
    Set<BlockState> getPlaceableOnlyOn(int blockStateIndex);

    /**
     * Called before an entity places a block.
     * Useful for entity dependent blockstates. (e.g. direction)
     * @param entity entity who placed the block
     * @param block the block being placed
     * @return if the block should be placed
     */
    boolean prepareForPlacement(Entity entity, Block block);

    /**
     * Called after the block is placed in the world.
     * @param entity the entity who placed this block
     * @param block the block being placed
     */
    void onPlace(Entity entity, Block block);

    /**
     * Called when the right click button is used against this block.
     * @param entity the entity who interacted with the block
     * @param block the block interacted with
     * @return if the item used to interact with this block should be called
     */
    boolean onInteract(Entity entity, Block block);

    /**
     * Called whenever an entity breaks this block.
     * @param entity the entity that broke this block
     * @param block the block broken
     */
    void onBreak(Entity entity, Block block);

    /**
     * Called whenever an entity moves onto this block.
     * @param entity the entity walking on the block
     * @param block the block walked on
     */
    void onWalkedOn(Entity entity, Block block);

    /**
     * Called whenever an entity walks off of this block.
     * @param entity the entity who walked off this block
     * @param block the block walked off of
     */
    void onWalkedOff(Entity entity, Block block);

    /**
     * Called every tick an entity is on this block.
     * @param entity the entity who is on this block
     * @param block the block the entity is standing on
     */
    void onStandingOn(Entity entity, Block block);

    /**
     * Called whenever a block next to this block is modified.
     * @param block the block being updated
     */
    void onUpdate(Block block);
    
}
