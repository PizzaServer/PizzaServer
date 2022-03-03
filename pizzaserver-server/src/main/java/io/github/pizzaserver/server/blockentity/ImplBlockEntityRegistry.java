package io.github.pizzaserver.server.blockentity;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.ServerState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImplBlockEntityRegistry implements BlockEntityRegistry {

    private final static Map<String, BlockEntityType<? extends Block>> entities = new HashMap<>();
    private final static Map<String, BlockEntityType<? extends Block>> entitiesByBlockTypeId = new HashMap<>();


    public void register(BlockEntityType<? extends Block> blockEntityType) {
        if (Server.getInstance().getState() != ServerState.REGISTERING) {
            throw new IllegalStateException("The server is not in the REGISTERING state");
        }

        if (entities.containsKey(blockEntityType.getId())) {
            for (String blockId : this.getBlockEntityType(blockEntityType.getId()).getBlockIds()) {
                entitiesByBlockTypeId.remove(blockId);
            }
        }

        entities.put(blockEntityType.getId(), blockEntityType);
        for (String blockId : blockEntityType.getBlockIds()) {
            entitiesByBlockTypeId.put(blockId, blockEntityType);
        }
    }

    @Override
    public BlockEntityType<? extends Block> getBlockEntityType(String blockEntityId) {
        if (!entities.containsKey(blockEntityId)) {
            throw new NullPointerException("The provided block entity type does not exist: " + blockEntityId);
        }
        return entities.get(blockEntityId);
    }

    @Override
    public Optional<BlockEntityType<? extends Block>> getBlockEntityType(Block blockType) {
        return Optional.ofNullable(entitiesByBlockTypeId.getOrDefault(blockType.getBlockId(), null));
    }

    @Override
    public boolean hasBlockEntityType(String blockEntityId) {
        return entitiesByBlockTypeId.containsKey(blockEntityId);
    }

}
