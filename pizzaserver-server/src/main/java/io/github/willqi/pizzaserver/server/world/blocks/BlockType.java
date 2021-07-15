package io.github.willqi.pizzaserver.server.world.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.commons.utils.BoundingBox;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.item.ItemToolType;
import io.github.willqi.pizzaserver.server.player.Player;

import java.util.Collections;
import java.util.Set;

public abstract class BlockType {

    private static final BoundingBox FULL_BLOCK_BOUNDING_BOX = new BoundingBox(new Vector3(0, 0, 0), new Vector3(1, 1, 1));


    /**
     * Namespace id of the block
     * e.g. minecraft:stone
     * @return the namespace id
     */
    public abstract String getBlockId();

    /**
     * Display name of the block
     * e.g. Stone
     * @return the display name
     */
    public abstract String getName();

    /**
     * Retrieve the NBTCompound for each of the block's states and the index associated with it.
     * The index is used for getBlockStateIndex in order to determine the current state of the block.
     * (e.g. the direction of stairs)
     * @return block states
     */
    public BiMap<NBTCompound, Integer> getBlockStates() {
        return HashBiMap.create();
    }

    public NBTCompound getBlockState(int index) {
        return this.getBlockStates().inverse().getOrDefault(index, null);
    }

    public int getBlockStateIndex(NBTCompound compound) {
        return this.getBlockStates().getOrDefault(compound, -1);
    }

    /**
     * Get the hitbox for this block
     * @return
     */
    public BoundingBox getHitBox() {
        return FULL_BLOCK_BOUNDING_BOX;
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
     * How hard this block is to explode.
     * @return strength of the block to explode
     */
    public float getBlastResistance() {
        return 0;
    }

    /**
     * How strong this block is to mine
     * @return strength of the block
     */
    public int getToughness() {
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
     * What should the block type do when it is pushed by a piston?
     */
    public enum PushResponse {
        DENY,
        ALLOW,
        BREAK
    }

}
