package io.github.willqi.pizzaserver.server.network.protocol.versions.v440;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.V431PacketBufferData;

public class V440PacketBufferData extends V431PacketBufferData {

    public static final BasePacketBufferData INSTANCE = new V440PacketBufferData();

    protected V440PacketBufferData() {
        this.registerEntityFlag(EntityMetaFlag.IS_PLAYING_DEAD, 97);
        this.registerEntityProperty(EntityMetaPropertyName.BUOYANCY_DATA, 122);
    }

}
