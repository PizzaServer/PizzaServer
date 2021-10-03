package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.level.world.blocks.BlockFace;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerAction;

/**
 * Sent to the server when the client does an action.
 */
public class PlayerActionPacket extends BaseBedrockPacket {

    public static final int ID = 0x24;

    private long entityRuntimeID;
    private PlayerAction actionType;
    private Vector3i vector3;
    private BlockFace face;

    public PlayerActionPacket() {
        super(ID);
    }

    public long getEntityRuntimeID() {
        return this.entityRuntimeID;
    }

    public void setEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeID = entityRuntimeID;
    }

    public PlayerAction getActionType() {
        return this.actionType;
    }

    public void setActionType(PlayerAction actionType) {
        this.actionType = actionType;
    }

    public Vector3i getVector3() {
        return this.vector3;
    }

    public void setVector3(Vector3i vector3) {
        this.vector3 = vector3;
    }

    public BlockFace getFace() {
        return this.face;
    }

    public void setFace(BlockFace face) {
        this.face = face;
    }
}
