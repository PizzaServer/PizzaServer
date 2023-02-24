package io.github.pizzaserver.api.blockentity.type;

public interface BlockEntitySoulCampfire extends BlockEntityCampfire {

    String ID = "SoulCampfire";

    @Override
    default String getId() {
        return ID;
    }

}
