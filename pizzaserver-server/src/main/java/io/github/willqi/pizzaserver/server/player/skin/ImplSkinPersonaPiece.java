package io.github.willqi.pizzaserver.server.player.skin;

import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPiece;

import java.util.UUID;

public class ImplSkinPersonaPiece implements SkinPersonaPiece {

    private final String id;
    private final String type;
    private final UUID packId;
    private final UUID productId;
    private final boolean isDefault;

    private ImplSkinPersonaPiece(String id, String type, UUID packId, UUID productId, boolean isDefault) {
        this.id = id;
        this.type = type;
        this.packId = packId;
        this.productId = productId;
        this.isDefault = isDefault;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public UUID getPackId() {
        return this.packId;
    }

    @Override
    public UUID getProductId() {
        return this.productId;
    }

    @Override
    public boolean isDefault() {
        return this.isDefault;
    }


    public static class Builder {

        private String id;
        private String type;
        private UUID packId;
        private UUID productId;
        private boolean isDefault;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setPackId(UUID packId) {
            this.packId = packId;
            return this;
        }

        public Builder setProductId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder setDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public ImplSkinPersonaPiece build() {
            return new ImplSkinPersonaPiece(this.id, this.type, this.packId, this.productId, this.isDefault);
        }

    }


}
