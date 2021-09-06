package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.api.item.types.BlockItemType;
import io.github.willqi.pizzaserver.api.item.types.ItemTypeID;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyType;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.player.skin.SkinAnimation;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPiece;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPieceTint;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.network.protocol.data.EntityLink;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.AuthoritativeInventorySlot;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class V419PacketBuffer extends BasePacketBuffer {

    public V419PacketBuffer(BaseMinecraftVersion version) {
        super(version);
    }

    public V419PacketBuffer(BaseMinecraftVersion version, int initialCapacity) {
        super(version, initialCapacity);
    }

    public V419PacketBuffer(BaseMinecraftVersion version, ByteBuf byteBuf) {
        super(version, byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V419PacketBuffer(this.getVersion(), buffer);
    }

    public BasePacketBufferData getData() {
        return V419PacketBufferData.INSTANCE;
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
        int itemData = ((itemStack.getDamage() << 8) | itemStack.getCount());
        this.writeVarInt(itemData);

        // Write NBT tag
        if (!itemStack.getCompoundTag().isEmpty()) {
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            try (NBTOutputStream stream = new NBTOutputStream(new VarIntDataOutputStream(resultStream))) {
                stream.writeCompound(itemStack.getCompoundTag());
            } catch (IOException exception) {
                throw new RuntimeException("Unable to write NBT tag", exception);
            }

            this.writeShortLE(-1);  // Item tag format?
            this.writeByte(1);  // Item version
            this.writeBytes(resultStream.toByteArray());
        } else {
            this.writeShortLE(0);
        }

        // Blocks this item can be placed on
        if (itemStack.getItemType() instanceof BlockItemType) {
            this.writeVarInt(itemStack.getBlocksCanPlaceOn().size());
            for (BaseBlockType placeableOnBlockType : itemStack.getBlocksCanPlaceOn()) {
                this.writeString(placeableOnBlockType.getBlockId());
            }
        } else {
            this.writeVarInt(0);
        }

        // Blocks this item can break
        this.writeVarInt(itemStack.getBlocksCanBreak().size());
        for (BaseBlockType blockType : itemStack.getBlocksCanBreak()) {
            this.writeString(blockType.getBlockId());
        }

        if (itemStack.getItemType().getItemId().equals(ItemTypeID.SHIELD)) {
            this.writeVarInt(0);    // TODO: blocking tick for shields? Investigate and serialize correctly
        }

        return this;
    }

    @Override
    public BasePacketBuffer writeSkin(Skin skin) {
        this.writeString(skin.getSkinId());
        this.writeString(skin.getSkinResourcePatch());
        this.writeIntLE(skin.getSkinWidth());
        this.writeIntLE(skin.getSkinHeight());
        this.writeByteArray(skin.getSkinData());

        this.writeIntLE(skin.getAnimations().size());
        for (SkinAnimation animation : skin.getAnimations()) {
            this.writeIntLE(animation.getSkinWidth());
            this.writeIntLE(animation.getSkinHeight());
            this.writeByteArray(animation.getSkinData());
            this.writeIntLE(animation.getType());
            this.writeFloatLE(animation.getFrame());
            this.writeIntLE(animation.getExpressionType());
        }

        this.writeIntLE(skin.getCapeWidth());
        this.writeIntLE(skin.getCapeHeight());
        this.writeByteArray(skin.getCapeData());

        this.writeString(skin.getGeometryData());
        this.writeString(skin.getAnimationData());
        this.writeBoolean(skin.isPremium());
        this.writeBoolean(skin.isPersona());
        this.writeBoolean(skin.isCapeOnClassic());
        this.writeString(skin.getCapeId());
        this.writeString(skin.getFullSkinId());
        this.writeString(skin.getArmSize());
        this.writeString(skin.getSkinColour());

        this.writeIntLE(skin.getPieces().size());
        for (SkinPersonaPiece personaPiece : skin.getPieces()) {
            this.writeString(personaPiece.getId());
            this.writeString(personaPiece.getType());
            this.writeString(personaPiece.getPackId().toString());
            this.writeBoolean(personaPiece.isDefault());
            this.writeString(personaPiece.getProductId() != null ? personaPiece.getProductId().toString() : "");
        }

        this.writeIntLE(skin.getTints().size());
        for (SkinPersonaPieceTint personaPieceTint : skin.getTints()) {
            this.writeString(personaPieceTint.getId());

            this.writeIntLE(personaPieceTint.getColors().size());
            for (String colour : personaPieceTint.getColors()) {
                this.writeString(colour);
            }
        }

        return this;
    }

    @Override
    public Skin readSkin() {
        Skin.Builder skinBuilder = new Skin.Builder()
                .setSkinId(this.readString())
                .setPlayFabId("")
                .setSkinResourcePatch(this.readString())
                .setSkinWidth(this.readIntLE())
                .setSkinHeight(this.readIntLE())
                .setSkinData(this.readByteArray());

        int animationsCount = this.readIntLE();
        List<SkinAnimation> animations = new ArrayList<>(animationsCount);
        for (int i = 0; i < animationsCount; i++) {
            SkinAnimation skinAnimation = new SkinAnimation.Builder()
                    .setSkinWidth(this.readIntLE())
                    .setSkinHeight(this.readIntLE())
                    .setSkinData(this.readByteArray())
                    .setType(this.readIntLE())
                    .setFrame((int)this.readFloatLE())
                    .setExpressionType(this.readIntLE())
                    .build();
            animations.add(skinAnimation);
        }
        skinBuilder.setAnimations(animations);

        skinBuilder.setCapeWidth(this.readIntLE())
                .setCapeHeight(this.readIntLE())
                .setCapeData(this.readByteArray())
                .setGeometryData(this.readString())
                .setAnimationData(this.readString())
                .setPremium(this.readBoolean())
                .setPersona(this.readBoolean())
                .setCapeOnClassic(this.readBoolean())
                .setCapeId(this.readString())
                .setFullSkinId(this.readString())
                .setArmSize(this.readString())
                .setSkinColour(this.readString());

        int piecesCount = this.readIntLE();
        List<SkinPersonaPiece> pieces = new ArrayList<>(piecesCount);
        for (int i = 0; i < piecesCount; i++) {
            SkinPersonaPiece.Builder pieceBuilder = new SkinPersonaPiece.Builder()
                    .setId(this.readString())
                    .setType(this.readString())
                    .setPackId(UUID.fromString(this.readString()))
                    .setDefault(this.readBoolean());
            String uuidStr = this.readString();
            pieceBuilder.setProductId(uuidStr.length() == 0 ? null : UUID.fromString(uuidStr));

            pieces.add(pieceBuilder.build());
        }
        skinBuilder.setPieces(pieces);

        int tintCount = this.readIntLE();
        List<SkinPersonaPieceTint> tints = new ArrayList<>(tintCount);
        for (int i = 0; i < tintCount; i++) {
            String id = this.readString();

            int colourCount = this.readIntLE();
            List<String> colours = new ArrayList<>(colourCount);
            for (int j = 0; j < colourCount; j++) {
                colours.add(this.readString());
            }

            SkinPersonaPieceTint tint = new SkinPersonaPieceTint(id, colours);
            tints.add(tint);
        }

        return skinBuilder
                .setTints(tints)
                .build();
    }

    @Override
    public ItemStack readItem() {
        int runtimeId = this.readVarInt();
        if (runtimeId == 0) {
            return ItemRegistry.getItem(BlockTypeID.AIR);
        }
        BaseItemType itemType = ItemRegistry.getItemType(this.getVersion().getItemName(runtimeId));

        // get count + damage
        int itemData = this.readVarInt();
        int count = itemData & 0xff;
        int damage = (itemData >> 8);

        ItemStack itemStack = new ItemStack(itemType, count, damage);

        NBTCompound tag = new NBTCompound();
        int tagLength = this.readShortLE();
        if (tagLength == -1) {  // Why does Microsoft allow -1 or a valid tag length for reading from the client but not writing to the client?...
            this.readByte();    // 0x01 - this is supposedly the item version
            tag = this.readNBTCompound();
        } else if (tagLength > 0) {
            tag = this.readNBTCompound();
        }
        itemStack.setCompoundTag(tag);

        int blocksCanPlaceCount = this.readVarInt();
        if (itemType instanceof BlockItemType) {
            Set<BaseBlockType> blocksCanPlaceOn = new HashSet<>(blocksCanPlaceCount);
            for (int i = 0; i < blocksCanPlaceCount; i++) {
                String blockId = this.readString();
                blocksCanPlaceOn.add(BlockRegistry.getBlockType(blockId));
            }
            itemStack.setBlocksCanPlaceOn(blocksCanPlaceOn);
        }

        int blocksCanBreakCount = this.readVarInt();
        Set<BaseBlockType> blocksCanBreak = new HashSet<>(blocksCanBreakCount);
        for (int i = 0; i < blocksCanBreakCount; i++) {
            String blockId = this.readString();
            blocksCanBreak.add(BlockRegistry.getBlockType(blockId));
        }
        itemStack.setBlocksCanBreak(blocksCanBreak);

        if (itemType.getItemId().equals(ItemTypeID.SHIELD)) {
            int blockingTicks = this.readVarInt();
            // TODO: blocking ticks for shields? Investigate and apply correctly.
        }
        return itemStack;
    }

    @Override
    public InventoryAction readInventoryAction(InventoryActionType actionType) {
        InventoryAction action;
        switch (actionType) {
            case TAKE:
                int takenAmount = this.readByte();
                action = new InventoryActionTake(this.readInventorySlot(), this.readInventorySlot(), takenAmount);
                break;
            case PLACE:
                int placedAmount = this.readByte();
                action = new InventoryActionPlace(this.readInventorySlot(), this.readInventorySlot(), placedAmount);
                break;
            case SWAP:
                action = new InventoryActionSwap(this.readInventorySlot(), this.readInventorySlot());
                break;
            case DROP:
                int droppedAmount = this.readByte();
                action = new InventoryActionDrop(this.readInventorySlot(), droppedAmount, this.readBoolean());
                break;
            case DESTROY:
                int destroyedAmount = this.readByte();
                action = new InventoryActionDestroy(this.readInventorySlot(), destroyedAmount);
                break;
            case CONSUME:
                int consumedAmount = this.readByte();
                action = new InventoryActionConsume(this.readInventorySlot(), consumedAmount);
                break;
            case CREATE:
                int createdSlot = this.readByte();
                action = new InventoryActionCreate(createdSlot);
                break;
            case LAB_TABLE_COMBINE:
                action = new InventoryActionLabTable();
                break;
            case BEACON_PAYMENT:
                action = new InventoryActionBeaconPayment(this.readVarInt(), this.readVarInt());
                break;
            case CRAFT_RECIPE:
                action = new InventoryActionCraftRecipe(this.readUnsignedVarInt());
                break;
            case AUTO_CRAFT_RECIPE:
                action = new InventoryActionAutoCraftRecipe(this.readUnsignedVarInt());
                break;
            case CRAFT_CREATIVE:
                action = new InventoryActionCraftCreative(this.readUnsignedVarInt());
                break;
            case CRAFT_NOT_IMPLEMENTED:
                action = new InventoryActionNotImplemented();
                break;
            case CRAFT_RESULTS_DEPRECATED:
                int craftedItemsAmount = this.readUnsignedVarInt();
                List<ItemStack> createdItems = new ArrayList<>(craftedItemsAmount);
                for (int i = 0; i < craftedItemsAmount; i++) {
                    createdItems.add(this.readItem());
                }
                int timesCreated = this.readByte();
                action = new InventoryActionCraftResultsDeprecated(createdItems, timesCreated);
                break;
            default:
                throw new IllegalStateException("Encountered unknown inventory action id: " + actionType);
        }
        return action;
    }

    @Override
    public AuthoritativeInventorySlot readInventorySlot() {
        int type = this.readByte();
        int slot = this.readByte();
        int stackNetworkId = this.readVarInt();
        return new AuthoritativeInventorySlot(InventorySlotType.values()[type], slot, stackNetworkId);
    }

    @Override
    public BasePacketBuffer writeEntityMetadata(EntityMetaData metaData) {
        // Filter for the flags we support
        Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> flags = metaData.getFlags();
        for (EntityMetaFlagCategory flagType : flags.keySet()) {
            Set<EntityMetaFlag> supportedFlags = flags.get(flagType).stream()
                    .filter(this.getData()::isEntityFlagSupported)
                    .collect(Collectors.toSet());

            flags.put(flagType, supportedFlags);
        }

        // Filter for the properties we support
        Map<EntityMetaPropertyName, EntityMetaProperty<?>> properties = new HashMap<>(metaData.getProperties());
        properties.keySet().removeIf(propertyName -> !this.getData().isEntityPropertySupported(propertyName));

        // Serialize all entries
        int totalEntries = flags.keySet().size() + properties.size();
        this.writeUnsignedVarInt(totalEntries);

        for (EntityMetaFlagCategory flagType : flags.keySet()) {
            this.writeUnsignedVarInt(this.getData().getEntityMetaFlagCategoryId(flagType));
            if (flagType == EntityMetaFlagCategory.PLAYER_FLAG) {
                byte flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1 << this.getData().getEntityFlagId(flag);
                }
                this.writeUnsignedVarInt(EntityMetaPropertyType.BYTE.ordinal());
                this.writeByte(flagValue);
            } else {
                long flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1L << this.getData().getEntityFlagId(flag);
                }
                this.writeUnsignedVarInt(EntityMetaPropertyType.LONG.ordinal());
                this.writeVarLong(flagValue);
            }
        }

        for (EntityMetaPropertyName propertyName : properties.keySet()) {
            this.writeUnsignedVarInt(this.getData().getEntityPropertyId(propertyName));
            this.writeUnsignedVarInt(propertyName.getType().ordinal());
            switch (propertyName.getType()) {
                case BYTE:
                    this.writeByte((Byte)properties.get(propertyName).getValue());
                    break;
                case SHORT:
                    this.writeShortLE((Short)properties.get(propertyName).getValue());
                    break;
                case INTEGER:
                    this.writeVarInt((Integer)properties.get(propertyName).getValue());
                    break;
                case FLOAT:
                    this.writeFloatLE((Float)properties.get(propertyName).getValue());
                    break;
                case LONG:
                    this.writeVarLong((Long)properties.get(propertyName).getValue());
                    break;
                case STRING:
                    this.writeString((String)properties.get(propertyName).getValue());
                    break;
                case NBT:
                    this.writeNBTCompound((NBTCompound)properties.get(propertyName).getValue());
                    break;
                case VECTOR3I:
                    Vector3i vector3i = (Vector3i)properties.get(propertyName).getValue();
                    this.writeVector3i(vector3i);
                    break;
                case VECTOR3:
                    Vector3 vector3 = (Vector3)properties.get(propertyName).getValue();
                    this.writeVector3(vector3);
                    break;
                default:
                    throw new UnsupportedOperationException("Missing implementation when encoding entity meta type " + propertyName.getType());
            }
        }

        return this;
    }

    @Override
    public BasePacketBuffer writeEntityLink(EntityLink entityLink) {
        this.writeVarLong(entityLink.getRiderUniqueEntityId());
        this.writeVarLong(entityLink.getTransportationUniqueEntityId());
        this.writeByte(entityLink.getType().ordinal());
        this.writeBoolean(entityLink.isImmediate());
        this.writeBoolean(entityLink.isRiderInitiated());

        return this;
    }

}
