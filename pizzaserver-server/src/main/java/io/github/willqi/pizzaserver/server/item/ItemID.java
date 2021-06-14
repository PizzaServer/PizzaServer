package io.github.willqi.pizzaserver.server.item;

public enum ItemID {

    ;

    private final String nameId;


    ItemID(String nameId) {
        this.nameId = nameId;
    }

    public String getNameId() {
        return this.nameId;
    }

}
