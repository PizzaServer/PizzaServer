package io.github.pizzaserver.api.blockentity.types;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.level.world.World;

import java.util.Set;

public interface BlockEntityType<T extends Block, B extends BlockEntity> {

    String getId();

    /**
     * Retrieve the blocks that this block entity is associated with.
     * @return block types
     */
    Set<String> getBlockIds();

    B create(T block);

    B deserializeDisk(World world, NbtMap diskNBT);

    NbtMap serializeForDisk(B blockEntity);

    NbtMap serializeForNetwork(NbtMap diskNBT);

    default NbtMap serializeForNetwork(B blockEntity) {
        return this.serializeForNetwork(this.serializeForDisk(blockEntity));
    }

}
