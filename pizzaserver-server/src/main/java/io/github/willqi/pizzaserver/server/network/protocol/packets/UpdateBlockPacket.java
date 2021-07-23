package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.world.blocks.APIBlock;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.world.blocks.Block;

import java.util.Collections;
import java.util.Set;

public class UpdateBlockPacket extends BedrockPacket {

    public static final int ID = 0x15;

    private APIBlock block;
    private Vector3i blockCoordinates;
    private int layer;

    private Set<Flag> flags = Collections.emptySet();


    public UpdateBlockPacket() {
        super(ID);
    }

    public APIBlock getBlock() {
        return this.block;
    }

    public void setBlock(APIBlock block) {
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
        PRIORITY
    }


}
