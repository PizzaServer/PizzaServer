package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.data.WorldEventType;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Sent by the server to send some sort of world effecting event.
 */
public class WorldEventPacket extends BaseBedrockPacket {

    public static final int ID = 0x19;

    private WorldEventType type;
    private Vector3 position;
    private int data;


    public WorldEventPacket() {
        super(ID);
    }

    public WorldEventType getType() {
        return this.type;
    }

    public void setType(WorldEventType type) {
        this.type = type;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }


}
