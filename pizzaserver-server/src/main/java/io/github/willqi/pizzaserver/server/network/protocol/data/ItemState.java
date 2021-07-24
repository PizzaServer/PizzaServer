package io.github.willqi.pizzaserver.server.network.protocol.data;

public class ItemState implements io.github.willqi.pizzaserver.api.network.protocol.data.ItemState {

    private final String nameId;
    private final int id;
    private final boolean isComponentBased; // TODO: Investigate the purpose of this


    public ItemState(String nameId, int id, boolean isComponentBased) {
        this.nameId = nameId;
        this.id = id;
        this.isComponentBased = isComponentBased;
    }

    @Override
    public String getItemId() {
        return this.nameId;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean isComponentBased() {
        return this.isComponentBased;
    }


}
