package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.item.ItemBlock;
import io.github.willqi.pizzaserver.server.item.ItemID;
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
    public BasePacketBuffer writeItem(Item item) {
        // network id
        this.writeVarInt(item.getId().ordinal()); // TODO: This probably isn't the proper item id. Find out how to get it

        // item damage + count
        int itemData = ((item.getDamage() << 8) | item.getCount());   // TODO: or maybe it's this id. The above id is just the network id. Does it affect anything?
        this.writeVarInt(itemData);

        // Write NBT tag
        if (item.getTag() != null) {
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            try {
                NBTOutputStream stream = new NBTOutputStream(new VarIntDataOutputStream(resultStream));
                stream.writeCompound(item.getTag());
            } catch (IOException exception) {
                throw new RuntimeException("Unable to write NBT tag", exception);
            }

            this.writeShortLE(resultStream.toByteArray().length);
            this.writeBytes(resultStream.toByteArray());
        } else {
            this.writeShortLE(0);
        }

        // Blocks this item can be placed on
        if (item instanceof ItemBlock) {
            ItemBlock blockItem = (ItemBlock)item;
            this.writeVarInt(blockItem.getBlocksCanBePlacedOn().size());
            for (ItemID itemId : blockItem.getBlocksCanBePlacedOn()) {
                this.writeString(itemId.getNameId());
            }
        } else {
            this.writeVarInt(0);
        }

        // Blocks this item can break
        this.writeVarInt(item.getBlocksCanBreak().size());
        for (ItemID itemId : item.getBlocksCanBreak()) {
            this.writeString(itemId.getNameId());
        }

        return this;
    }

}
