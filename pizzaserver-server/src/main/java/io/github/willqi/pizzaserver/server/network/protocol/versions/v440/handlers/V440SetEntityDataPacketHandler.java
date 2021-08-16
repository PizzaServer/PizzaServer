package io.github.willqi.pizzaserver.server.network.protocol.versions.v440.handlers;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers.V428SetEntityDataPacketHandler;

public class V440SetEntityDataPacketHandler extends V428SetEntityDataPacketHandler {

    public V440SetEntityDataPacketHandler() {
        this.supportedFlags.put(EntityMetaFlag.IS_PLAYING_DEAD, 97);

        this.supportedProperties.put(EntityMetaPropertyName.BUOYANCY_DATA, 122);
    }

}
