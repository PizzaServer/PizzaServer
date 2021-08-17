package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.types.components.BlockItemComponent;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class V419PacketBuffer extends BasePacketBuffer {

    public V419PacketBuffer(int initialCapacity) {
        super(initialCapacity);
    }

    public V419PacketBuffer(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V419PacketBuffer(buffer);
    }

    @Override
    public BasePacketBuffer writeItem(NetworkItemStackData data) {
        ItemStack itemStack = data.getItemStack();

        // network id
        this.writeVarInt(data.getRuntimeId());

        // item damage + count
        int itemData = ((itemStack.getDamage() << 8) | itemStack.getCount());
        this.writeVarInt(itemData);

        // Write NBT tag
        if (itemStack.getCompoundTag() != null) {
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            try {
                NBTOutputStream stream = new NBTOutputStream(new VarIntDataOutputStream(resultStream));
                stream.writeCompound(itemStack.getCompoundTag());
            } catch (IOException exception) {
                throw new RuntimeException("Unable to write NBT tag", exception);
            }

            this.writeShortLE(resultStream.toByteArray().length);
            this.writeBytes(resultStream.toByteArray());
        } else {
            this.writeShortLE(0);
        }

        // Blocks this item can be placed on
        if (itemStack instanceof BlockItemComponent) {
            BaseBlockType blockType = ((BlockItemComponent)itemStack).getBlock().getBlockType();
            this.writeVarInt(blockType.getPlaceableOnlyOn().size());
            for (BaseBlockType placeableOnBlockType : blockType.getPlaceableOnlyOn()) {
                this.writeString(placeableOnBlockType.getBlockId());
            }
        } else {
            this.writeVarInt(0);
        }

        // Blocks this item can break
        this.writeVarInt(itemStack.getItemType().getOnlyBlocksCanBreak().size());
        for (BaseBlockType blockType : itemStack.getItemType().getOnlyBlocksCanBreak()) {
            this.writeString(blockType.getBlockId());
        }

        return this;
    }

}
