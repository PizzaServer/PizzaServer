package io.github.willqi.pizzaserver.api.player.skin;

public class SkinAnimation {

    private final int type;
    private final int frame;

    private final int skinHeight;
    private final int skinWidth;
    private final byte[] skinData;

    private final int expressionType;


    private SkinAnimation(int type, int frame, int skinHeight, int skinWidth, byte[] skinData, int expressionType) {
        this.type = type;
        this.frame = frame;
        this.skinHeight = skinHeight;
        this.skinWidth = skinWidth;
        this.skinData = skinData;
        this.expressionType = expressionType;
    }

    public int getType() {
        return this.type;
    }

    public int getFrame() {
        return this.frame;
    }

    public int getSkinHeight() {
        return this.skinHeight;
    }

    public int getSkinWidth() {
        return this.skinWidth;
    }

    public byte[] getSkinData() {
        return this.skinData;
    }

    public int getExpressionType() {
        return this.expressionType;
    }


    public static class Builder {

        private int type;
        private int frame;

        private int skinHeight;
        private int skinWidth;
        private byte[] skinData;

        private int expressionType;

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setFrame(int frame) {
            this.frame = frame;
            return this;
        }

        public Builder setSkinHeight(int skinHeight) {
            this.skinHeight = skinHeight;
            return this;
        }

        public Builder setSkinWidth(int skinWidth) {
            this.skinWidth = skinWidth;
            return this;
        }

        public Builder setSkinData(byte[] data) {
            this.skinData = data;
            return this;
        }

        public Builder setExpressionType(int expressionType) {
            this.expressionType = expressionType;
            return this;
        }

        public SkinAnimation build() {
            return new SkinAnimation(this.type,
                    this.frame,
                    this.skinHeight,
                    this.skinWidth,
                    this.skinData,
                    this.expressionType);
        }

    }

}
