package io.github.pizzaserver.api.item.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;
import io.github.pizzaserver.api.player.Player;

public class BaseItemBehavior<T extends Item> implements ItemBehavior<T> {

    @Override
    public boolean onInteract(Player player, T item, Block block, BlockFace blockFace, Vector3f clickPosition) {
        return true;
    }

    @Override
    public void onInteract(Player player, T item, Entity entity) {}

    @Override
    public void onBreak(Player player, T item, Block block) {}

}
