package io.github.willqi.pizzaserver.server.network.protocol.data;

public class EntityLink {

    private final long riderUniqueEntityId;
    private final long transportationUniqueEntityId;
    private final Type type;
    private final boolean immediate;
    private final boolean riderInitiated;


    public EntityLink(long riderUniqueEntityId,
                      long transportationUniqueEntityId,
                      Type type,
                      boolean immediate,
                      boolean riderInitiated) {
        this.riderUniqueEntityId = riderUniqueEntityId;
        this.transportationUniqueEntityId = transportationUniqueEntityId;
        this.type = type;
        this.immediate = immediate;
        this.riderInitiated = riderInitiated;
    }

    public long getRiderUniqueEntityId() {
        return this.riderUniqueEntityId;
    }

    public long getTransportationUniqueEntityId() {
        return this.transportationUniqueEntityId;
    }

    public Type getType() {
        return this.type;
    }

    // TODO: What is this?
    public boolean isImmediate() {
        return this.immediate;
    }

    public boolean isRiderInitiated() {
        return this.riderInitiated;
    }


    public enum Type {
        REMOVE,
        RIDER,
        PASSENGER
    }

}
