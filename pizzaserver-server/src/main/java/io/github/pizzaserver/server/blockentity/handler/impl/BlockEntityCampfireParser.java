package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntityCampfire;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityCampfire;

public class BlockEntityCampfireParser extends BaseBlockEntityParser<BlockEntityCampfire> {

    @Override
    public BlockEntityCampfire fromDiskNBT(World world, NbtMap nbt) {
        return this.create(new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"))));
    }

    @Override
    public BlockEntityCampfire create(BlockLocation location) {
        return new ImplBlockEntityCampfire(location);
    }

}
