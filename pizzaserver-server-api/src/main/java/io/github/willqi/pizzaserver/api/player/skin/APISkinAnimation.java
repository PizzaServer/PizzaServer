package io.github.willqi.pizzaserver.api.player.skin;

public interface APISkinAnimation {

    // TODO: convert to enum
    int getType();

    int getFrame();

    int getSkinHeight();

    int getSkinWidth();

    byte[] getSkinData();

}
