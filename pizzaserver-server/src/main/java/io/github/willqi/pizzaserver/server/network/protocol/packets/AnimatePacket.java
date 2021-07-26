package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class AnimatePacket extends ImplBedrockPacket {

    public static final int ID = 0x2c;
    private int actionID;
    private long entityRuntimeID;

    public AnimatePacket() {
        super(ID);
    }

    public int getActionID() {
        return actionID;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public long getEntityRuntimeID() {
        return entityRuntimeID;
    }

    public void setEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeID = entityRuntimeID;
    }
}
