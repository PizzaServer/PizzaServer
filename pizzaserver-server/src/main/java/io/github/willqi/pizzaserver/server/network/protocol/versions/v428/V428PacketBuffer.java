package io.github.willqi.pizzaserver.server.network.protocol.versions.v428;

import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.player.skin.SkinAnimation;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPiece;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPieceTint;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryActionMineBlock;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryActionType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.V422PacketBuffer;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class V428PacketBuffer extends V422PacketBuffer {

    public V428PacketBuffer(BaseMinecraftVersion version) {
        super(version);
    }

    public V428PacketBuffer(BaseMinecraftVersion version, int initialCapacity) {
        super(version, initialCapacity);
    }

    public V428PacketBuffer(BaseMinecraftVersion version, ByteBuf byteBuf) {
        super(version, byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V428PacketBuffer(this.getVersion(), buffer);
    }

    @Override
    public BasePacketBufferData getData() {
        return V428PacketBufferData.INSTANCE;
    }

    @Override
    public InventoryAction readInventoryAction(InventoryActionType actionType) {
        if (actionType == InventoryActionType.MINE_BLOCK) {
            int unknown = this.readVarInt();
            int predictedDurability = this.readVarInt();
            int networkStackId = this.readVarInt();
            return new InventoryActionMineBlock(unknown, predictedDurability, networkStackId);
        } else {
            return super.readInventoryAction(actionType);
        }
    }

    @Override
    public BasePacketBuffer writeSkin(Skin skin) {
        this.writeString(skin.getSkinId());
        this.writeString(skin.getPlayFabId());          // v428 introduces playfab id
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
                .setPlayFabId(this.readString())        // v428 introduces playfab id
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

}
