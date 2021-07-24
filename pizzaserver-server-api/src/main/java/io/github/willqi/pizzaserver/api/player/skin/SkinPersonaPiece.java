package io.github.willqi.pizzaserver.api.player.skin;

import java.util.UUID;

public interface SkinPersonaPiece {

    String getId();

    // TODO: possible to turn this into a enum?
    String getType();

    UUID getPackId();

    UUID getProductId();

    boolean isDefault();

}
