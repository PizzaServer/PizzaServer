package io.github.willqi.pizzaserver.server.network.protocol.versions.v422;

import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions.InventoryAction;
import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions.InventoryActionCraftRecipeOptional;
import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions.InventoryActionType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419PacketBuffer;
import io.netty.buffer.ByteBuf;

public class V422PacketBuffer extends V419PacketBuffer {

    public V422PacketBuffer(BaseMinecraftVersion version) {
        super(version);
    }

    public V422PacketBuffer(BaseMinecraftVersion version, int initialCapacity) {
        super(version, initialCapacity);
    }

    public V422PacketBuffer(BaseMinecraftVersion version, ByteBuf byteBuf) {
        super(version, byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V422PacketBuffer(this.getVersion(), buffer);
    }

    @Override
    public BasePacketBufferData getData() {
        return V422PacketBufferData.INSTANCE;
    }

    @Override
    public InventoryAction readInventoryAction(InventoryActionType actionType) {
        if (actionType == InventoryActionType.CRAFT_RECIPE_OPTIONAL) {
            int networkId = this.readUnsignedVarInt();
            int customNameIndex = this.readIntLE();
            return new InventoryActionCraftRecipeOptional(networkId, customNameIndex);
        } else {
            return super.readInventoryAction(actionType);
        }
    }

}
