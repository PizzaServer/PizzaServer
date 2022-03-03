package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.player.Player;

public interface BlockEntityContainer<T extends Block> extends BlockEntity<T> {

    BlockEntityInventory<? extends BlockEntity<T>> getInventory();

    void showOpenAnimation();

    void showOpenAnimation(Player player);

    void showCloseAnimation();

    void showCloseAnimation(Player player);

}
