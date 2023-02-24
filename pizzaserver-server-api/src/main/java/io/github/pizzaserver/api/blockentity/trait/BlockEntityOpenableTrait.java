package io.github.pizzaserver.api.blockentity.trait;

import io.github.pizzaserver.api.player.Player;

public interface BlockEntityOpenableTrait {

    void showOpenAnimation();

    void showOpenAnimation(Player player);

    void showCloseAnimation();

    void showCloseAnimation(Player player);

}
