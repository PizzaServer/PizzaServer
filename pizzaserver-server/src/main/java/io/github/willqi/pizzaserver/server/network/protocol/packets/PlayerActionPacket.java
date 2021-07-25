package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerAction;

public class PlayerActionPacket extends BedrockPacket {

    public static final int ID = 0x24;

    private long entityRuntimeID;
    private PlayerAction actionType;
    private Vector3 vector3;
    private int face;

    public PlayerActionPacket() {
        super(ID);
    }

    public long getEntityRuntimeID() {
        return entityRuntimeID;
    }

    public void setEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeID = entityRuntimeID;
    }

    public PlayerAction getActionType() {
        return actionType;
    }

    public void setActionType(PlayerAction actionType) {
        this.actionType = actionType;
    }

    public Vector3 getVector3() {
        return vector3;
    }

    public void setVector3(Vector3 vector3) {
        this.vector3 = vector3;
    }

    /**
     * Does not return the face of the block on a value of 0-5, it returns the clients calculation of the face
     * @return Face calculated by client
     */
    @Deprecated
    public int getFace() {
        return face;
    }

    //Will need this method later once some way of faces are planned
    //public static BlockFace fromIndex(int index) {
    //    return VALUES[MathHelper.abs(index % VALUES.length)];
    //}

    public void setFace(int face) {
        this.face = face;
    }
}
