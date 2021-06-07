package io.github.willqi.pizzaserver.server.network.protocol.data;

public class ItemState {

    private final String nameId;
    private final int id;
    private final boolean isComponentBased; // TODO: Investigate the purpose of this


    public ItemState(String nameId, int id, boolean isComponentBased) {
        this.nameId = nameId;
        this.id = id;
        this.isComponentBased = isComponentBased;
    }

    public String getNameId() {
        return this.nameId;
    }

    public int getId() {
        return this.id;
    }

    public boolean isComponentBased() {
        return this.isComponentBased;
    }


}
