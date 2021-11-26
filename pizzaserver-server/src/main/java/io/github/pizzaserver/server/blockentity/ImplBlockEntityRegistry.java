package io.github.pizzaserver.server.blockentity;

import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImplBlockEntityRegistry implements BlockEntityRegistry {

    private final static Map<String, BlockEntityType> entities = new HashMap<>();
    private final static Map<String, BlockEntityType> entitiesByBlockTypeId = new HashMap<>();

    @Override
    public void register(BlockEntityType blockEntityType) {
        if (entities.containsKey(blockEntityType.getId())) {
            entitiesByBlockTypeId.remove(entities.get(blockEntityType.getId()).getBlockType().getBlockId());
        }

        entities.put(blockEntityType.getId(), blockEntityType);
        entitiesByBlockTypeId.put(blockEntityType.getBlockType().getBlockId(), blockEntityType);
    }

    @Override
    public BlockEntityType getBlockEntityType(String blockEntityId) {
        if (!entities.containsKey(blockEntityId)) {
            throw new NullPointerException("The provided block entity type does not exist: " + blockEntityId);
        }
        return entities.get(blockEntityId);
    }

    /**
     * Retrieves the block entity type equivalent of a block type if one exists.
     * @param blockType block type to look for
     * @return block entity type if any exists
     */
    @Override
    public Optional<BlockEntityType> getBlockEntityType(BlockType blockType) {
        return Optional.ofNullable(entitiesByBlockTypeId.getOrDefault(blockType.getBlockId(), null));
    }

}
