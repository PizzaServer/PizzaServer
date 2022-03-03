package io.github.pizzaserver.server.blockentity.handler;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public abstract class BaseBlockEntityParser<T extends BlockEntity<? extends Block>> implements BlockEntityParser<T> {

    @Override
    public NbtMap toDiskNBT(T blockEntity) {
        return NbtMap.builder()
                .putString("id", blockEntity.getId())
                .putInt("x", blockEntity.getLocation().getX())
                .putInt("y", blockEntity.getLocation().getY())
                .putInt("z", blockEntity.getLocation().getZ())
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(T blockEntity) {
        return NbtMap.builder()
                .putString("id", blockEntity.getId())
                .putInt("x", blockEntity.getLocation().getX())
                .putInt("y", blockEntity.getLocation().getY())
                .putInt("z", blockEntity.getLocation().getZ())
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return NbtMap.builder()
                .putString("id", diskNBT.getString("id"))
                .putInt("x", diskNBT.getInt("x"))
                .putInt("y", diskNBT.getInt("y"))
                .putInt("z", diskNBT.getInt("z"))
                .build();
    }

}
