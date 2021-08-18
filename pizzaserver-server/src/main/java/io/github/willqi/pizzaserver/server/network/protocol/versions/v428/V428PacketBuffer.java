package io.github.willqi.pizzaserver.server.network.protocol.versions.v428;

import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.player.skin.SkinAnimation;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPiece;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPieceTint;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.V422PacketBuffer;
import io.netty.buffer.ByteBuf;

public class V428PacketBuffer extends V422PacketBuffer {

    public V428PacketBuffer() {}

    public V428PacketBuffer(int initialCapacity) {
        super(initialCapacity);
    }

    public V428PacketBuffer(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V428PacketBuffer(buffer);
    }

    @Override
    protected BasePacketBufferData getData() {
        return V428PacketBufferData.INSTANCE;
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

}
