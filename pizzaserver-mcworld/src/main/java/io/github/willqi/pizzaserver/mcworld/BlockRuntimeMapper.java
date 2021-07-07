package io.github.willqi.pizzaserver.mcworld;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public interface BlockRuntimeMapper {

    int getBlockRuntimeId(String name, NBTCompound states);

}
