package io.github.willqi.pizzaserver.api.network.protocol.packets;

/**
 * Sent by the server to change the time of the world.
 * This is not required to be sent constantly as the client progresses on it's own.
 */
public class SetTimePacket extends BaseBedrockPacket {

    public static final int ID = 0x0a;

    private int time;


    public SetTimePacket() {
        super(ID);
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
