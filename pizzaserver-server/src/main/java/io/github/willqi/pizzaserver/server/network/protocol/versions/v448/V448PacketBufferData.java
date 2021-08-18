package io.github.willqi.pizzaserver.server.network.protocol.versions.v448;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.V440PacketBufferData;

public class V448PacketBufferData extends V440PacketBufferData {

    public static final BasePacketBufferData INSTANCE = new V448PacketBufferData();


    protected V448PacketBufferData() {
        this.registerEntityFlag(EntityMetaFlag.IN_ASCENDABLE_BLOCK, 98)
            .registerEntityFlag(EntityMetaFlag.OVER_DESCENDABLE_BLOCK, 99);
    }

}
