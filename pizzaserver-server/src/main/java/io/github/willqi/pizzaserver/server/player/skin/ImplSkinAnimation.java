package io.github.willqi.pizzaserver.server.player.skin;

import io.github.willqi.pizzaserver.api.player.skin.SkinAnimation;

public class ImplSkinAnimation implements SkinAnimation {

    private final int type;
    private final int frame;

    private final int skinHeight;
    private final int skinWidth;
    private final byte[] skinData;

    private ImplSkinAnimation(int type, int frame, int skinHeight, int skinWidth, byte[] skinData) {
        this.type = type;
        this.frame = frame;
        this.skinHeight = skinHeight;
        this.skinWidth = skinWidth;
        this.skinData = skinData;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public int getFrame() {
        return this.frame;
    }

    @Override
    public int getSkinHeight() {
        return this.skinHeight;
    }

    @Override
    public int getSkinWidth() {
        return this.skinWidth;
    }

    @Override
    public byte[] getSkinData() {
        return this.skinData;
    }


    public static class Builder {

        private int type;
        private int frame;

        private int skinHeight;
        private int skinWidth;
        private byte[] skinData;

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

        public ImplSkinAnimation build() {
            return new ImplSkinAnimation(this.type, this.frame, this.skinHeight, this.skinWidth, this.skinData);
        }

    }

}