package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.trait.HorizontalDirectionalTrait;
import io.github.pizzaserver.api.entity.Entity;

public class HorizontalBlockBehavior<T extends HorizontalDirectionalTrait> extends BaseBlockBehavior<T> {

    @Override
    public boolean prepareForPlacement(Entity entity, T block, BlockFace face, Vector3f clickPosition) {
        block.setDirection(entity.getHorizontalDirection().opposite());
        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

}
