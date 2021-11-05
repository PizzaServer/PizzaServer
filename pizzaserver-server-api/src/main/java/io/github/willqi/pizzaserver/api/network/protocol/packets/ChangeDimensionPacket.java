package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.level.world.data.Dimension;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Sent to update the current dimension shown to the player.
 */
public class ChangeDimensionPacket extends BaseBedrockPacket {

    public static final int ID = 0x3d;

    private Dimension dimension;
    private Vector3 position;
    private boolean respawn;


    public ChangeDimensionPacket() {
        super(ID);
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    /**
     * If this packet is in response to the client's respawn action request after the player dies.
     * @return if it is in response
     */
    public boolean isRespawnResponse() {
        return this.respawn;
    }

    /**
     * Set if this packet is in response to the client's respawn action request after the player dies.
     * @param isResponse if it is in response
     */
    public void setRespawnResponse(boolean isResponse) {
        this.respawn = isResponse;
    }

}
