package io.github.pizzaserver.api.blockentity.types;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.level.world.World;

import java.util.Set;

public interface BlockEntityType {

    String getId();

    /**
     * Retrieve the block types that this block entity is associated with.
     * @return block types
     */
    Set<BlockType> getBlockTypes();

    BlockEntity create(Block block);

    BlockEntity deserializeDisk(World world, NbtMap diskNBT);

    NbtMap serializeForDisk(BlockEntity blockEntity);

    NbtMap serializeForNetwork(NbtMap diskNBT);

    default NbtMap serializeForNetwork(BlockEntity blockEntity) {
        return this.serializeForNetwork(this.serializeForDisk(blockEntity));
    }

}
