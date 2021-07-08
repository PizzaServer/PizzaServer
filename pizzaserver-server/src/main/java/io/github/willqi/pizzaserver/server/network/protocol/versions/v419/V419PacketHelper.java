package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.item.ItemBlock;
import io.github.willqi.pizzaserver.server.item.ItemID;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class V419PacketHelper extends PacketHelper {

    public final static PacketHelper INSTANCE = new V419PacketHelper();

    @Override
    public void writeItem(Item item, ByteBuf buffer) {

        // network id
        VarInts.writeInt(buffer, item.getId().ordinal());   // TODO: This probably isn't the proper item id. Find out how to get it

        // item damage + count
        int itemData = ((item.getDamage() << 8) | item.getCount());   // TODO: or maybe it's this id. The above id is just the network id. Does it affect anything?
        VarInts.writeInt(buffer, itemData);

        // Write NBT tag
        if (item.getTag() != null) {
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            try {
                NBTOutputStream stream = new NBTOutputStream(new VarIntDataOutputStream(resultStream));
                stream.writeCompound(item.getTag());
            } catch (IOException exception) {
                throw new RuntimeException("Unable to write NBT tag", exception);
            }

            buffer.writeShortLE(resultStream.toByteArray().length);
            buffer.writeBytes(resultStream.toByteArray());
        } else {
            buffer.writeShortLE(0);
        }

        // Blocks this item can be placed on
        if (item instanceof ItemBlock) {
            ItemBlock blockItem = (ItemBlock)item;
            VarInts.writeInt(buffer, blockItem.getBlocksCanBePlacedOn().size());
            for (ItemID itemId : blockItem.getBlocksCanBePlacedOn()) {
                this.writeString(itemId.getNameId(), buffer);
            }
        } else {
            VarInts.writeInt(buffer, 0);
        }

        // Blocks this item can break
        VarInts.writeInt(buffer, item.getBlocksCanBreak().size());
        for (ItemID itemId : item.getBlocksCanBreak()) {
            this.writeString(itemId.getNameId(), buffer);
        }

    }

}
