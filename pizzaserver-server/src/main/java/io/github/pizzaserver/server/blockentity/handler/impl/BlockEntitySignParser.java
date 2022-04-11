package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntitySign;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntitySign;

public class BlockEntitySignParser extends BaseBlockEntityParser<BlockEntitySign> {

    @Override
    public BlockEntitySign create(BlockLocation location) {
        return new ImplBlockEntitySign(location);
    }

    @Override
    public BlockEntitySign fromDiskNBT(World world, NbtMap nbt) {
        BlockLocation location = new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));

        BlockEntitySign sign = this.create(location);
        sign.setText(nbt.getString("Text", ""));
        return sign;
    }

    @Override
    public NbtMap toDiskNBT(BlockEntitySign blockEntity) {
        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putString("Text", blockEntity.getText())
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return diskNBT;
    }

}
