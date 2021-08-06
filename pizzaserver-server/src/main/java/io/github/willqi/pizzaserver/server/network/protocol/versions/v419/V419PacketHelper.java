package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.player.skin.SkinAnimation;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPiece;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPieceTint;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.item.ItemBlock;
import io.github.willqi.pizzaserver.server.item.ItemID;
import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class V419PacketHelper extends BasePacketHelper {

    public final static BasePacketHelper INSTANCE = new V419PacketHelper();


    public V419PacketHelper() {
        this.supportedExperiments.add(Experiment.DATA_DRIVEN_ITEMS);
    }

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

    @Override
    public void writeSkin(ByteBuf buffer, Skin skin) {
        this.writeString(skin.getSkinId(), buffer);
        this.writeString(skin.getPlayFabId(), buffer);
        this.writeString(skin.getSkinResourcePatch(), buffer);
        buffer.writeIntLE(skin.getSkinWidth());
        buffer.writeIntLE(skin.getSkinHeight());
        this.writeByteArray(skin.getSkinData(), buffer);

        buffer.writeIntLE(skin.getAnimations().size());
        for (SkinAnimation animation : skin.getAnimations()) {
            buffer.writeIntLE(animation.getSkinWidth());
            buffer.writeIntLE(animation.getSkinHeight());
            this.writeByteArray(animation.getSkinData(), buffer);
            buffer.writeIntLE(animation.getType());
            buffer.writeFloatLE(animation.getFrame());
            buffer.writeIntLE(animation.getExpressionType());
        }

        buffer.writeIntLE(skin.getCapeWidth());
        buffer.writeIntLE(skin.getCapeHeight());
        this.writeByteArray(skin.getCapeData(), buffer);

        this.writeString(skin.getGeometryData(), buffer);
        this.writeString(skin.getAnimationData(), buffer);
        buffer.writeBoolean(skin.isPremium());
        buffer.writeBoolean(skin.isPersona());
        buffer.writeBoolean(skin.isCapeOnClassic());
        this.writeString(skin.getCapeId(), buffer);
        this.writeString(skin.getFullSkinId(), buffer);
        this.writeString(skin.getArmSize(), buffer);
        this.writeString(skin.getSkinColour(), buffer);

        buffer.writeIntLE(skin.getPieces().size());
        for (SkinPersonaPiece personaPiece : skin.getPieces()) {
            this.writeString(personaPiece.getId(), buffer);
            this.writeString(personaPiece.getType(), buffer);
            this.writeString(personaPiece.getPackId().toString(), buffer);
            buffer.writeBoolean(personaPiece.isDefault());
            this.writeString(personaPiece.getProductId().toString(), buffer);
        }

        buffer.writeIntLE(skin.getTints().size());
        for (SkinPersonaPieceTint personaPieceTint : skin.getTints()) {
            this.writeString(personaPieceTint.getId(), buffer);

            buffer.writeIntLE(personaPieceTint.getColors().size());
            for (String colour : personaPieceTint.getColors()) {
                this.writeString(colour, buffer);
            }
        }
    }

    @Override
    public Skin readSkin(ByteBuf buffer) {
        Skin.Builder skinBuilder = new Skin.Builder()
                .setSkinId(this.readString(buffer))
                .setPlayFabId(this.readString(buffer))
                .setSkinResourcePatch(this.readString(buffer))
                .setSkinWidth(buffer.readIntLE())
                .setSkinHeight(buffer.readIntLE())
                .setSkinData(this.readByteArray(buffer));

        int animationsCount = buffer.readIntLE();
        List<SkinAnimation> animations = new ArrayList<>(animationsCount);
        for (int i = 0; i < animationsCount; i++) {
            SkinAnimation skinAnimation = new SkinAnimation.Builder()
                    .setSkinWidth(buffer.readIntLE())
                    .setSkinHeight(buffer.readIntLE())
                    .setSkinData(this.readByteArray(buffer))
                    .setType(buffer.readIntLE())
                    .setFrame((int)buffer.readFloatLE())
                    .setExpressionType(buffer.readIntLE())
                    .build();
            animations.add(skinAnimation);
        }
        skinBuilder.setAnimations(animations);

        skinBuilder.setCapeWidth(buffer.readIntLE())
                .setCapeHeight(buffer.readIntLE())
                .setCapeData(this.readByteArray(buffer))
                .setGeometryData(this.readString(buffer))
                .setAnimationData(this.readString(buffer))
                .setPremium(buffer.readBoolean())
                .setPersona(buffer.readBoolean())
                .setCapeOnClassic(buffer.readBoolean())
                .setCapeId(this.readString(buffer))
                .setFullSkinId(this.readString(buffer))
                .setArmSize(this.readString(buffer))
                .setSkinColour(this.readString(buffer));

        int piecesCount = buffer.readIntLE();
        List<SkinPersonaPiece> pieces = new ArrayList<>(piecesCount);
        for (int i = 0; i < piecesCount; i++) {
            SkinPersonaPiece piece = new SkinPersonaPiece.Builder()
                    .setId(this.readString(buffer))
                    .setType(this.readString(buffer))
                    .setPackId(UUID.fromString(this.readString(buffer)))
                    .setDefault(buffer.readBoolean())
                    .setProductId(UUID.fromString(this.readString(buffer)))
                    .build();
            pieces.add(piece);
        }
        skinBuilder.setPieces(pieces);

        int tintCount = buffer.readIntLE();
        List<SkinPersonaPieceTint> tints = new ArrayList<>(tintCount);
        for (int i = 0; i < tintCount; i++) {
            String id = this.readString(buffer);

            int colourCount = buffer.readIntLE();
            List<String> colours = new ArrayList<>(colourCount);
            for (int j = 0; j < colourCount; j++) {
                colours.add(this.readString(buffer));
            }

            SkinPersonaPieceTint tint = new SkinPersonaPieceTint(id, colours);
            tints.add(tint);
        }

        return skinBuilder
                .setTints(tints)
                .build();
    }

}
