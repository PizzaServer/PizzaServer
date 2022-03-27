package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockCauldron;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityCauldron;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.chunks.Chunk;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeCauldron implements BlockEntityType<BlockCauldron> {

    @Override
    public String getId() {
        return BlockEntityCauldron.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.CAULDRON);
    }

    @Override
    public BlockEntityCauldron create(BlockCauldron block) {
        return new BlockEntityCauldron(block);
    }

    @Override
    public BlockEntityCauldron deserializeDisk(Chunk chunk, NbtMap diskNBT) {
        Vector3i coordinates = Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"));
        return new BlockEntityCauldron((BlockCauldron) chunk.getBlock(coordinates));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity<BlockCauldron> blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getBlock().getX())
                .putInt("y", blockEntity.getBlock().getY())
                .putInt("z", blockEntity.getBlock().getZ())
                .putShort("PotionType", (short) -1)
                .putShort("PotionId", (short) -1)
                .putBoolean("IsSplash", false)
                .build();
        // TODO: proper serialization
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return diskNBT;
    }

}
