package io.github.willqi.pizzaserver.player.data.skin;

public class SkinPersonaPiece {

    private final String id;
    private final String type;
    private final String packId;
    private final String productId;
    private final boolean isDefault;

    private SkinPersonaPiece(String id, String type, String packId, String productId, boolean isDefault) {
        this.id = id;
        this.type = type;
        this.packId = packId;
        this.productId = productId;
        this.isDefault = isDefault;
        System.out.println(this.packId + " pack id");
        System.out.println(this.productId + " product id");
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getPackId() {
        return this.packId;
    }

    public String getProductId() {
        return this.productId;
    }

    public boolean isDefault() {
        return this.isDefault;
    }


    public static class Builder {

        private String id;
        private String type;
        private String packId;
        private String productId;
        private boolean isDefault;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setPackId(String packId) {
            this.packId = packId;
            return this;
        }

        public Builder setProductId(String productId) {
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
