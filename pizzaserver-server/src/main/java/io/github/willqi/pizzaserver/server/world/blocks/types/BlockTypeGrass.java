package io.github.willqi.pizzaserver.server.world.blocks.types;

public class BlockTypeGrass extends BlockTypeFullSolid {

    @Override
    public String getBlockId() {
        return BlockTypeID.GRASS;
    }

    @Override
    public String getName() {
        return "Grass";
    }

}
