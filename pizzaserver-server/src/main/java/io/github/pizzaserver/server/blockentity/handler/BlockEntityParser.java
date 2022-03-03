package io.github.pizzaserver.server.blockentity.handler;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;

public interface BlockEntityParser<T extends BlockEntity<? extends Block>>  {

    default T fromDiskNBT(World world, NbtMap nbt) {
        BlockLocation location = new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));

        return this.create(location);
    }

    T create(BlockLocation location);

    NbtMap toDiskNBT(T blockEntity);

    default NbtMap toNetworkNBT(T blockEntity) {
        return this.toNetworkNBT(this.toDiskNBT(blockEntity));
    }

    NbtMap toNetworkNBT(NbtMap diskNBT);

}
