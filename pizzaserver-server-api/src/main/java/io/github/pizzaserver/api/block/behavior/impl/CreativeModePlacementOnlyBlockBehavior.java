package io.github.pizzaserver.api.block.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;

/**
 * Blocks with this behavior can only be placed in creative mode.
 */
public class CreativeModePlacementOnlyBlockBehavior<T extends Block> extends BaseBlockBehavior<T> {

    @Override
    public boolean prepareForPlacement(Entity entity, T block, BlockFace face, Vector3f clickPosition) {
        return (entity instanceof Player) && ((Player) entity).getGamemode() == Gamemode.CREATIVE;
    }

}
