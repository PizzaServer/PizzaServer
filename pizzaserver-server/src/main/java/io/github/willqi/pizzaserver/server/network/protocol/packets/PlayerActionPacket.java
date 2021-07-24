package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.Server;

public class PlayerActionPacket extends BedrockPacket {

    public static final int START_BREAK = 0;
    public static final int ABORT_BREAK = 1;
    public static final int STOP_BREAK = 2;
    public static final int GET_UPDATED_BLOCK = 3;
    public static final int DROP_ITEM = 4;
    public static final int START_SLEEPING = 5;
    public static final int STOP_SLEEPING = 6;
    public static final int RESPAWN = 7;
    public static final int JUMP = 8;
    public static final int START_SPRINT = 9;
    public static final int STOP_SPRINT = 10;
    public static final int START_SNEAK = 11;
    public static final int STOP_SNEAK = 12;
    public static final int DIMENSION_CHANGE_REQUEST = 13; //sent when dying in different dimension
    public static final int DIMENSION_CHANGE_ACK = 14; //sent when spawning in a different dimension to tell the server we spawned
    public static final int START_GLIDE = 15;
    public static final int STOP_GLIDE = 16;
    public static final int BUILD_DENIED = 17;
    public static final int CONTINUE_BREAK = 18;
    public static final int SET_ENCHANTMENT_SEED = 20;
    public static final int START_SWIMMING = 21;
    public static final int STOP_SWIMMING = 22;
    public static final int START_SPIN_ATTACK = 23;
    public static final int STOP_SPIN_ATTACK = 24;
    public static final int INTERACT_BLOCK = 25;

    private long entityRuntimeID;
    private int actionType;
    private Vector3 vector3;
    private int face;

    public static int ID = 0x24;
    public PlayerActionPacket() {
        super(ID);
    }

    public long getEntityRuntimeID() {
        return entityRuntimeID;
    }

    public void setEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeID = entityRuntimeID;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public Vector3 getVector3() {
        return vector3;
    }

    public void setVector3(Vector3 vector3) {
        this.vector3 = vector3;
        Server.getInstance().getLogger().info(String.valueOf(vector3));
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
