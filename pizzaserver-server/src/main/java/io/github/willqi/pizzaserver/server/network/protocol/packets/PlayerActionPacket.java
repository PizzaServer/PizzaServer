package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.Server;

public class PlayerActionPacket extends BedrockPacket {

    public static final int ID = 0x24;

    private long entityRuntimeID;
    private int actionType;
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

    public enum Action {
        START_BREAK(0),
        ABORT_BREAK(1),
        STOP_BREAK(2),
        GET_UPDATED_BLOCK(3),
        DROP_ITEM(4),
        START_SLEEPING(5),
        STOP_SLEEPING(6),
        RESPAWN(7),
        JUMP(8),
        START_SPRINT(9),
        STOP_SPRINT(10),
        START_SNEAK(11),
        STOP_SNEAK(12),
        DIMENSION_CHANGE_REQUEST(13),
        DIMENSION_CHANGE_ACK(14),
        START_GLIDE(15),
        STOP_GLIDE(16),
        BUILD_DENIED(17),
        CONTINUE_BREAK(18),
        SET_ENCHANTMENT_SEED(20),
        START_SWIMMING(21),
        STOP_SWIMMING(22),
        START_SPIN_ATTACK(23),
        STOP_SPIN_ATTACK(24),
        INTERACT_BLOCK(25);

        public final int id;
        Action(int id) {
            this.id = id;
        }
    }
}
