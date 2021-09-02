package io.github.willqi.pizzaserver.server.network.protocol.versions.v431;

import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.api.item.types.BlockItemType;
import io.github.willqi.pizzaserver.api.item.types.ItemTypeID;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.V428PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class V431PacketBuffer extends V428PacketBuffer {

    public V431PacketBuffer(BaseMinecraftVersion version) {
        super(version);
    }

    public V431PacketBuffer(BaseMinecraftVersion version, int initialCapacity) {
        super(version, initialCapacity);
    }

    public V431PacketBuffer(BaseMinecraftVersion version, ByteBuf byteBuf) {
        super(version, byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V431PacketBuffer(this.getVersion(), buffer);
    }

    @Override
    public BasePacketBufferData getData() {
        return V431PacketBufferData.INSTANCE;
    }

    @Override
    public BasePacketBuffer writeItem(ItemStack itemStack) {
        int runtimeId = this.getVersion().getItemRuntimeId(itemStack.getItemType().getItemId());
        if (runtimeId == 0) {
            this.writeByte(0);
            return this;
        }

        // item id
        this.writeVarInt(runtimeId);

        // item damage + count
        this.writeShortLE(itemStack.getCount());
        this.writeUnsignedVarInt(itemStack.getDamage());

        // network id
        boolean usingNetworkId = itemStack.getNetworkId() > 0;  // cannot be air/missing network id
        this.writeBoolean(usingNetworkId);
        if (usingNetworkId) {
            this.writeVarInt(itemStack.getNetworkId());
        }

        // write block runtime id
        if (itemStack.getItemType() instanceof BlockItemType) {
            BlockItemType blockItemType = (BlockItemType)itemStack.getItemType();

            String blockId = blockItemType.getBlockType().getBlockId();
            NBTCompound state = blockItemType.getBlockType().getBlockState(itemStack.getDamage());
            int blockRuntimeId = this.getVersion().getBlockRuntimeId(blockId, state);

            this.writeVarInt(blockRuntimeId);
        } else {
            this.writeVarInt(0);
        }

        BasePacketBuffer innerItemDataBuf = this.createInstance(ByteBufAllocator.DEFAULT.buffer());
        try {
            // Write NBT tag
            if (!itemStack.getCompoundTag().isEmpty()) {
                innerItemDataBuf.writeShortLE(-1);  // Item tag format? length?
                innerItemDataBuf.writeByte(1);      // item version
                innerItemDataBuf.writeLENBTCompound(itemStack.getCompoundTag());
            } else {
                innerItemDataBuf.writeShortLE(0);
            }

            // Blocks this item can be placed on
            if (itemStack.getItemType() instanceof BlockItemType) {
                innerItemDataBuf.writeIntLE(itemStack.getBlocksCanPlaceOn().size());
                for (BaseBlockType placeableOnBlockType : itemStack.getBlocksCanPlaceOn()) {
                    innerItemDataBuf.writeStringLE(placeableOnBlockType.getBlockId());
                }
            } else {
                innerItemDataBuf.writeIntLE(0);
            }

            // Blocks this item can break
            innerItemDataBuf.writeIntLE(itemStack.getBlocksCanBreak().size());
            for (BaseBlockType blockType : itemStack.getBlocksCanBreak()) {
                innerItemDataBuf.writeStringLE(blockType.getBlockId());
            }

            if (itemStack.getItemType().getItemId().equals(ItemTypeID.SHIELD)) {
                innerItemDataBuf.writeLongLE(0); // TODO: blocking tick for shields? Investigate and serialize correctly
            }

            this.writeUnsignedVarInt(innerItemDataBuf.readableBytes());
            this.writeBytes(innerItemDataBuf);
        } finally {
            innerItemDataBuf.release();
        }

        return this;
    }

    @Override
    public ItemStack readItem() {
        int runtimeId = this.readVarInt();
        if (runtimeId == 0) {
            return ItemRegistry.getItem(BlockTypeID.AIR);
        }
        BaseItemType itemType = ItemRegistry.getItemType(this.getVersion().getItemName(runtimeId));

        // get count + damage
        int count = this.readShortLE();
        int damage = this.readUnsignedVarInt();

        // network id
        int networkId = -1;
        boolean usingNetworkId = this.readBoolean();
        if (usingNetworkId) {   // using network id
            networkId = this.readVarInt();
        }

        ItemStack itemStack = new ItemStack(itemType, count, damage, networkId);
        this.readVarInt();  // block runtime id: ignored.


        BasePacketBuffer innerItemDataBuf = this.readSlice(this.readUnsignedVarInt());

        try (LittleEndianDataInputStream dataInputStream = new LittleEndianDataInputStream(new ByteBufInputStream(innerItemDataBuf))) {
            // Read NBT tag
            int tagLength = dataInputStream.readShort();
            NBTCompound tag = new NBTCompound();
            if (tagLength == -1) {
                dataInputStream.readByte();    // 0x01 - this is supposedly the item version
                tag = innerItemDataBuf.readLENBTCompound();
            } else if (tagLength > 0) {
                tag = innerItemDataBuf.readLENBTCompound();
            }
            itemStack.setCompoundTag(tag);

            // blocks this block can be placed on
            int blocksCanPlaceCount = dataInputStream.readInt();
            if (itemType instanceof BlockItemType) {
                Set<BaseBlockType> blocksCanPlaceOn = new HashSet<>(blocksCanPlaceCount);
                for (int i = 0; i < blocksCanPlaceCount; i++) {
                    String blockId = dataInputStream.readUTF();
                    blocksCanPlaceOn.add(BlockRegistry.getBlockType(blockId));
                }
                itemStack.setBlocksCanPlaceOn(blocksCanPlaceOn);
            }

            // blocks this item can break
            int blocksCanBreakCount = dataInputStream.readInt();
            Set<BaseBlockType> blocksCanBreak = new HashSet<>(blocksCanBreakCount);
            for (int i = 0; i < blocksCanBreakCount; i++) {
                String blockId = dataInputStream.readUTF();
                blocksCanBreak.add(BlockRegistry.getBlockType(blockId));
            }
            itemStack.setBlocksCanBreak(blocksCanBreak);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to parse item data", exception);
        }

        if (itemType.getItemId().equals(ItemTypeID.SHIELD)) {
            long blockingTicks = innerItemDataBuf.readLongLE();
            // TODO: blocking ticks for shields? Investigate and apply correctly.
        }

        return itemStack;
    }

}
