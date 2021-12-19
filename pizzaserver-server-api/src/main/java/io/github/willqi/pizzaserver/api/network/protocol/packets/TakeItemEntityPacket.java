package io.github.willqi.pizzaserver.api.network.protocol.packets;

/**
 * Sent by the server to show the item take animation.
 */
public class TakeItemEntityPacket extends BaseBedrockPacket {

    public static final int ID = 0x11;

    private long itemRuntimeEntityId;
    private long runtimeEntityId;


    public TakeItemEntityPacket() {
        super(ID);
    }

    public long getItemRuntimeEntityId() {
        return this.itemRuntimeEntityId;
    }

    public void setItemRuntimeEntityId(long itemRuntimeEntityId) {
        this.itemRuntimeEntityId = itemRuntimeEntityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

}
