package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

import java.util.Collections;
import java.util.Set;

/**
 * Sent by the server to update a single block in the world.
 */
public class UpdateBlockPacket extends BaseBedrockPacket {

    public static final int ID = 0x15;

    private Block block;
    private Vector3i blockCoordinates;
    private int layer;

    private Set<Flag> flags = Collections.emptySet();


    public UpdateBlockPacket() {
        super(ID);
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Vector3i getBlockCoordinates() {
        return this.blockCoordinates;
    }

    public void setBlockCoordinates(Vector3i coordinates) {
        this.blockCoordinates = coordinates;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public Set<Flag> getFlags() {
        return this.flags;
    }

    public void setFlags(Set<Flag> flags) {
        this.flags = flags;
    }


    public enum Flag {
        NEIGHBOURS, // Most likely sends a block state update to nearby blocks
        NETWORK,
        NO_GRAPHIC,
        NOT_USED,
        PRIORITY
    }


}
