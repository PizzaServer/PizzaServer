package io.github.pizzaserver.server.blockentity;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImplBlockEntityRegistry implements BlockEntityRegistry {

    private final static Map<String, BlockEntityType> entities = new HashMap<>();
    private final static Map<String, BlockEntityType> entitiesByBlockTypeId = new HashMap<>();


    public void register(BlockEntityType blockEntityType) {
        if (entities.containsKey(blockEntityType.getId())) {
            for (Block block : this.getBlockEntityType(blockEntityType.getId()).getBlocks()) {
                entitiesByBlockTypeId.remove(block.getBlockId());
            }
        }

        entities.put(blockEntityType.getId(), blockEntityType);
        for (Block block : blockEntityType.getBlocks()) {
            entitiesByBlockTypeId.put(block.getBlockId(), blockEntityType);
        }
    }

    @Override
    public BlockEntityType getBlockEntityType(String blockEntityId) {
        if (!entities.containsKey(blockEntityId)) {
            throw new NullPointerException("The provided block entity type does not exist: " + blockEntityId);
        }
        return entities.get(blockEntityId);
    }

    @Override
    public Optional<BlockEntityType> getBlockEntityType(Block blockType) {
        return Optional.ofNullable(entitiesByBlockTypeId.getOrDefault(blockType.getBlockId(), null));
    }

    @Override
    public boolean hasBlockEntityType(String blockEntityId) {
        return entitiesByBlockTypeId.containsKey(blockEntityId);
    }

}
