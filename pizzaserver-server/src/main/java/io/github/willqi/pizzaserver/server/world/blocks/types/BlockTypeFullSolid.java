package io.github.willqi.pizzaserver.server.world.blocks.types;

import io.github.willqi.pizzaserver.commons.utils.BoundingBox;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * A block type with a full block boundary box
 */
public abstract class BlockTypeFullSolid extends BlockType {

    private static final BoundingBox HIT_BOX = new BoundingBox(new Vector3(0, 0, 0), new Vector3(1, 1, 1));


    @Override
    public BoundingBox getHitBox() {
        return HIT_BOX;
    }
    
}
