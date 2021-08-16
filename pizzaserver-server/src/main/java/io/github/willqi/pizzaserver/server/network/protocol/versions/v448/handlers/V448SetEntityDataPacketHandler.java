package io.github.willqi.pizzaserver.server.network.protocol.versions.v448.handlers;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428SetEntityDataPacketHandler;

public class V448SetEntityDataPacketHandler extends V428SetEntityDataPacketHandler {

    public V448SetEntityDataPacketHandler() {
        this.supportedFlags.put(EntityMetaFlag.IN_ASCENDABLE_BLOCK, 98);
        this.supportedFlags.put(EntityMetaFlag.OVER_DESCENDABLE_BLOCK, 99);
    }

}
