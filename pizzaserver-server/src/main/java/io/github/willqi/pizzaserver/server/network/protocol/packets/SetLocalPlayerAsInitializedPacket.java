package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class SetLocalPlayerAsInitializedPacket extends BedrockNetworkPacket {

    public static final int ID = 0x71;

    private long runtimeEntityId;


    public SetLocalPlayerAsInitializedPacket() {
        super(ID);
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

}
