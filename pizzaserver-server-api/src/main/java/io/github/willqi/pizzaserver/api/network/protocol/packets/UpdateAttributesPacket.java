package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.entity.attributes.Attribute;

import java.util.Collections;
import java.util.Set;

/**
 * Sent to update the attributes of a player entity (e.g. hunger, xp, health. etc)
 */
public class UpdateAttributesPacket extends BaseBedrockPacket {

    public static final int ID = 0x1d;

    private long runtimeEntityId;
    private Set<Attribute> attributes = Collections.emptySet();

    private long tick;


    public UpdateAttributesPacket() {
        super(ID);
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public void setRuntimeEntityId(long id) {
        this.runtimeEntityId = id;
    }

    public Set<Attribute> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public long getTick() {
        return this.tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }


}
