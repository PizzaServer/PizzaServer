package io.github.willqi.pizzaserver.server.world.blocks.types;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.commons.data.id.Identifier;
import io.github.willqi.pizzaserver.commons.data.storage.IdentityKey;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.item.ItemToolType;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.blocks.Block;
import io.github.willqi.pizzaserver.server.world.blocks.BlockLoot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public abstract class BlockType {

    private static final HashBiMap<NBTCompound, Integer> EMPTY_BLOCK_STATES = HashBiMap.create(new HashMap<NBTCompound, Integer>(){
        {
            this.put(new NBTCompound("states"), 0);
        }
    });


    /**
     * Namespace id of the block
     * e.g. minecraft:stone
     * @return the namespace id
     */
    public abstract IdentityKey<? extends BlockType> getBlockId();

    /**
     * Display name of the block
     * e.g. Stone
     * @return the display name
     */
    public abstract String getName();

    /**
     * Retrieve the {@link NBTCompound} for each of the block's states and the index associated with it.
     * The {@link NBTCompound}s returned should match the data in block_states.nbt in order to work correctly.
     * The index is used for getBlockStateIndex in order to determine the current state of the block.
     * (e.g. the direction of stairs)
     * Each {@link NBTCompound} must be named "states" for proper serialization/deserialization
     *
     * By default there is a empty block state if this method is not overridden.
     * @return block states
     */
    public BiMap<NBTCompound, Integer> getBlockStates() {
        return EMPTY_BLOCK_STATES;
    }

    public NBTCompound getBlockState(int index) {
        return this.getBlockStates().inverse().getOrDefault(index, null);
    }

    public int getBlockStateIndex(NBTCompound compound) {
        return this.getBlockStates().getOrDefault(compound, -1);
    }

    /**
     * The amount of light this block will absorb
     * @return the amount of light absorbed by this block
     */
    public int getLightAbsorption() {
        return 0;
    }

    /**
     * The amount of light this block will emit
     * @return a decimal in the range of [0, 1]
     */
    public float getLightEmission() {
        return 0;
    }

    /**
     * What this block does when pushed (e.g. a piston)
     * @return push response
     */
    public PushResponse getPushResponse() {
        return PushResponse.ALLOW;
    }

    /**
     * If a player head is in this block, should their oxygen start depleting
     * @return if the block has oxygen
     */
    public boolean hasOxygen() {
        return true;
    }

    /**
     * If the block type is solid
     * @return if the block type is solid
     */
    public boolean isSolid() {
        return true;
    }

    /**
     * How strong this block is to mine
     * @return strength of the block
     */
    public int getToughness() {
        return 0;
    }

    /**
     * Get the origin position of the block
     * @return
     */
    public float[] getOrigin() {
        return new float[]{ -8f, 0f, -8f };
    }

    /**
     * Retrieve the height of this BlockType
     * @return height
     */
    public float getHeight() {
        return 16f;
    }

    /**
     * Retrieve the width of this BlockType
     * @return width
     */
    public float getWidth() {
        return 16f;
    }

    /**
     * Retrieve the length of this BlockType
     * @return length
     */
    public float getLength() {
        return 16f;
    }

    /**
     * How hard this block is to explode.
     * @return strength of the block to explode
     */
    public float getBlastResistance() {
        return 0;
    }

    /**
     * How likely the block will be destroyed by flames when on fire.
     * @return chance of being destroyed
     */
    public int getBurnOdds() {
        return 0;
    }

    /**
     * How likely the block will catch flame when next to a fire.
     * @return Chance of catching fire
     */
    public int getFlameOdds() {
        return 0;
    }

    /**
     * Retrieve the friction entities should receive on this block
     * @return friction of this block type
     */
    public float getFriction() {
        return 0.1f;
    }

    /**
     * Retrieve the geometry to use for this block type.
     * If null is returned then it will use the default block geometry
     * @return block geometry id
     */
    public String getGeometry() {
        return null;
    }

    /**
     * Retrieve the map colour this block displays on a map
     * If null is returned then it will use the default map colour
     * @return hex value of the color to display on the map
     */
    public String getMapColour() {
        return null;
    }

    /**
     * If this block allows players to jump while on it
     * @return if players can jump while on this block
     */
    public boolean allowsJumping() {
        return true;
    }

    /**
     * Retrieve the rotation of this block
     * @return rotation of the block
     */
    public int[] getRotation() {
        return new int[]{ 0, 0, 0 };
    }

    /**
     * If this block should fall if there's no block supporting it
     * @return if the block should fall with no supports
     */
    public boolean hasGravity() {
        return false;
    }

    /**
     * The percentage of fall damage that should be removed when gets damaged by fall damage while falling onto this block
     * @return a float between 0-1 that describes the ignored fall damage percent
     */
    public float getFallDamageReduction() {
        return 0;
    }

    /**
     * Retrieve the item tool types that should be used against this block
     * in order for the item drops to spawn when the block is broken
     * @return tools that will result in a block drop
     */
    public Set<ItemToolType> getCorrectTools() {
        return Collections.singleton(ItemToolType.ANY);
    }

    /**
     * Retrieve the loot that should be dropped when this block is broken
     * @return set of the item loot to consider when dropping loot
     */
    public Set<BlockLoot> getLoot(Player player) {
        return Collections.emptySet();
    }

    /**
     * Called when the right click button is used against this block
     * @param player the player who interacted with the block
     * @param block the block interacted with
     */
    public void onInteract(Player player, Block block) {}

    /**
     * Called every tick when a player is walking on this block type
     * @param player the player walking on the block
     * @param block the block being walked on
     */
    public void onWalkedOn(Player player, Block block) {}

    /**
     * Called whenever a block next to this block is modified
     * @param player the player who caused the update
     * @param block the block being walked on
     */
    public void onUpdate(Player player, Block block) {}


    /**
     * What the block type should do when it is pushed by a piston
     */
    public enum PushResponse {
        DENY,

        ALLOW,

        /**
         * Prevents this block from sticking to a sticky piston
         */
        ALLOW_NO_STICKY,

        /**
         * Break this block when pushed by a piston
         */
        BREAK
    }

}
