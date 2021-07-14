package io.github.willqi.pizzaserver.server.entity.meta.properties;

public enum EntityMetaPropertyName {

    ;


    private final EntityMetaPropertyType type;


    EntityMetaPropertyName(EntityMetaPropertyType type) {
        this.type = type;
    }

    public EntityMetaPropertyType getType() {
        return this.type;
    }

}
