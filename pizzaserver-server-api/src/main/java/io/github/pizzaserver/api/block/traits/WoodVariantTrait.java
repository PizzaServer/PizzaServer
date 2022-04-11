package io.github.pizzaserver.api.block.traits;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.WoodType;

/**
 * Any block that has wooden variants.
 */
public interface WoodVariantTrait extends Block {

    WoodType getWoodType();

    void setWoodType(WoodType woodType);

}
