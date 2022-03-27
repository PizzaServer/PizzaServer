package io.github.pizzaserver.api.blockentity.types;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;

import java.util.Set;

public interface BlockEntityType<T extends Block> {

    String getId();

    /**
     * Retrieve the blocks that this block entity is associated with.
     * @return block types
     */
    Set<String> getBlockIds();

    BlockEntity<T> create(T block);

    BlockEntity<T> deserializeDisk(Chunk chunk, NbtMap diskNBT);

    NbtMap serializeForDisk(BlockEntity<T> blockEntity);

    NbtMap serializeForNetwork(NbtMap diskNBT);

    default NbtMap serializeForNetwork(BlockEntity<T> blockEntity) {
        return this.serializeForNetwork(this.serializeForDisk(blockEntity));
    }

}
