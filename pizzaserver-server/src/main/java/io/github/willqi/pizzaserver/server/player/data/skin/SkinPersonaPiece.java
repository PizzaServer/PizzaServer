package io.github.willqi.pizzaserver.server.player.data.skin;

import java.util.UUID;

public class SkinPersonaPiece {

    private final String id;
    private final String type;
    private final UUID packId;
    private final UUID productId;
    private final boolean isDefault;

    private SkinPersonaPiece(String id, String type, UUID packId, UUID productId, boolean isDefault) {
        this.id = id;
        this.type = type;
        this.packId = packId;
        this.productId = productId;
        this.isDefault = isDefault;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public UUID getPackId() {
        return this.packId;
    }

    public UUID getProductId() {
        return this.productId;
    }

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

        public SkinPersonaPiece build() {
            return new SkinPersonaPiece(this.id, this.type, this.packId, this.productId, this.isDefault);
        }

    }


}
