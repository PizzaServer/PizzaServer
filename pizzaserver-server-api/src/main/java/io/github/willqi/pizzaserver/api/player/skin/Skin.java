package io.github.willqi.pizzaserver.api.player.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Skin {

    private String skinId;
    private String fullSkinId;
    private String playFabId;
    private String skinResourcePatch;
    private String geometryData;
    private int skinHeight;
    private int skinWidth;
    private byte[] skinData;

    private int capeHeight;
    private int capeWidth;
    private byte[] capeData;
    private String capeId;
    private boolean capeOnClassic;

    private String animationData;
    private String armSize;
    private String skinColour;
    private List<SkinAnimation> animations;
    private List<SkinPersonaPiece> pieces;
    private List<SkinPersonaPieceTint> tints;
    private boolean isPersona;

    private boolean premium;
    private boolean trusted;


    // Persona skin
    private Skin(
            String skinId,
            String fullSkinId,
            String playFabId,
            String skinResourcePatch,
            String geometryData,
            int skinHeight,
            int skinWidth,
            byte[] skinData,
            String capeId,
            boolean capeOnClassic,
            int capeHeight,
            int capeWidth,
            byte[] capeData,
            String armSize,
            String skinColour,
            String animationData,
            List<SkinAnimation> animations,
            List<SkinPersonaPiece> pieces,
            List<SkinPersonaPieceTint> tints,
            boolean premium,
            boolean trusted
    ) {
        this.skinId = skinId;
        this.fullSkinId = fullSkinId;
        this.playFabId = playFabId;
        this.skinResourcePatch = skinResourcePatch;
        this.geometryData = geometryData;
        this.skinHeight = skinHeight;
        this.skinWidth = skinWidth;
        this.skinData = skinData;

        this.capeId = capeId;
        this.capeOnClassic = capeOnClassic;
        this.capeHeight = capeHeight;
        this.capeWidth = capeWidth;
        this.capeData = capeData;

        this.premium = premium;
        this.trusted = trusted;

        this.isPersona = true;
        this.armSize = armSize;
        this.skinColour = skinColour;
        this.animationData = animationData;
        this.animations = Collections.unmodifiableList(animations);
        this.pieces = Collections.unmodifiableList(pieces);
        this.tints = Collections.unmodifiableList(tints);
    }

    // Base skin
    private Skin(
            String skinId,
            String fullSkinId,
            String playFabId,
            String skinResourcePatch,
            String geometryData,
            int skinHeight,
            int skinWidth,
            byte[] skinData,
            String capeId,
            boolean capeOnClassic,
            int capeHeight,
            int capeWidth,
            byte[] capeData,
            boolean premium,
            boolean trusted
    ) {
        this.skinId = skinId;
        this.fullSkinId = fullSkinId;
        this.playFabId = playFabId;
        this.skinResourcePatch = skinResourcePatch;
        this.geometryData = geometryData;
        this.skinHeight = skinHeight;
        this.skinWidth = skinWidth;
        this.skinData = skinData;

        this.capeId = capeId;
        this.capeOnClassic = capeOnClassic;
        this.capeHeight = capeHeight;
        this.capeWidth = capeWidth;
        this.capeData = capeData;

        this.premium = premium;
        this.trusted = trusted;

        this.isPersona = false;
        this.armSize = "";
        this.skinColour = "#O";
        this.animationData = "";
        this.animations = Collections.emptyList();
        this.pieces = Collections.emptyList();
        this.tints = Collections.emptyList();
    }

    public String getSkinId() {
        return this.skinId;
    }

    public void setSkinId(String skinId) {
        this.skinId = skinId;
    }

    public String getFullSkinId() {
        return this.fullSkinId;
    }

    public void setFullSkinId(String fullSkinId) {
        this.fullSkinId = fullSkinId;
    }

    public String getPlayFabId() {
        return this.playFabId;
    }

    public void setPlayFabId(String playFabId) {
        this.playFabId = playFabId;
    }

    public String getSkinResourcePatch() {
        return this.skinResourcePatch;
    }

    public void setSkinResourcePatch(String resourcePatch) {
        this.skinResourcePatch = resourcePatch;
    }

    public String getGeometryData() {
        return this.geometryData;
    }

    public void setGeometryData(String geometryData) {
        this.geometryData = geometryData;
    }

    public int getSkinHeight() {
        return this.skinHeight;
    }

    public void setSkinHeight(int skinHeight) {
        this.skinHeight = skinHeight;
    }

    public int getSkinWidth() {
        return this.skinWidth;
    }

    public void setSkinWidth(int skinWidth) {
        this.skinWidth = skinWidth;
    }

    public byte[] getSkinData() {
        return this.skinData;
    }

    public void setSkinData(byte[] data) {
        this.skinData = data;
    }

    public int getCapeHeight() {
        return this.capeHeight;
    }

    public void setCapeHeight(int capeHeight) {
        this.capeHeight = capeHeight;
    }

    public int getCapeWidth() {
        return this.capeWidth;
    }

    public void setCapeWidth(int capeWidth) {
        this.capeWidth = capeWidth;
    }

    public byte[] getCapeData() {
        return this.capeData;
    }

    public void setCapeData(byte[] capeData) {
        this.capeData = capeData;
    }

    public String getCapeId() {
        return this.capeId;
    }

    public void setCapeId(String capeId) {
        this.capeId = capeId;
    }

    public boolean isCapeOnClassic() {
        return this.capeOnClassic;
    }

    public void setCapeOnClassic(boolean isOnClassic) {
        this.capeOnClassic = isOnClassic;
    }

    public String getAnimationData() {
        return this.animationData;
    }

    public void setAnimationData(String data) {
        this.animationData = data;
    }

    public String getArmSize() {
        return this.armSize;
    }

    public void setArmSize(String armSize) {
        this.armSize = armSize;
    }

    public String getSkinColour() {
        return this.skinColour;
    }

    public void setSkinColour(String skinColour) {
        this.skinColour = skinColour;
    }

    public List<SkinAnimation> getAnimations() {
        return this.animations;
    }

    public void setAnimations(List<SkinAnimation> animations) {
        this.animations = animations;
    }

    public List<SkinPersonaPiece> getPieces() {
        return this.pieces;
    }

    public void setPieces(List<SkinPersonaPiece> pieces) {
        this.pieces = pieces;
    }

    public List<SkinPersonaPieceTint> getTints() {
        return this.tints;
    }

    public void setTints(List<SkinPersonaPieceTint> tints) {
        this.tints = tints;
    }

    public boolean isPersona() {
        return this.isPersona;
    }

    public void setPersona(boolean isPersona) {
        this.isPersona = isPersona;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public void setPremium(boolean isPremium) {
        this.premium = isPremium;
    }

    public boolean isTrusted() {
        return this.trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }


    public static class Builder {

        private String skinId;
        private String fullSkinId;
        private String playFabId;
        private String skinResourcePatch;
        private String geometryData;
        private int skinHeight;
        private int skinWidth;
        private byte[] skinData;

        private int capeHeight;
        private int capeWidth;
        private byte[] capeData;
        private String capeId;
        private boolean capeOnClassic;

        private String animationData;
        private String armSize;
        private String skinColor;
        private List<SkinAnimation> animations = new ArrayList<>();
        private List<SkinPersonaPiece> pieces = new ArrayList<>();
        private List<SkinPersonaPieceTint> tints = new ArrayList<>();
        private boolean isPersona;

        private boolean premium;
        private boolean trusted;

        public Builder setSkinId(String skinId) {
            this.skinId = skinId;
            return this;
        }

        public Builder setFullSkinId(String fullSkinId) {
            this.fullSkinId = fullSkinId;
            return this;
        }

        public Builder setPlayFabId(String playFabId) {
            this.playFabId = playFabId;
            return this;
        }

        public Builder setSkinResourcePatch(String skinResourcePatch) {
            this.skinResourcePatch = skinResourcePatch;
            return this;
        }

        public Builder setGeometryData(String geometryData) {
            this.geometryData = geometryData;
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

        public Builder setCapeId(String capeId) {
            this.capeId = capeId;
            return this;
        }

        public Builder setCapeOnClassic(boolean classic) {
            this.capeOnClassic = classic;
            return this;
        }

        public Builder setAnimationData(String animationData) {
            this.animationData = animationData;
            return this;
        }

        public Builder setArmSize(String armSize) {
            this.armSize = armSize;
            return this;
        }

        public Builder setSkinColour(String skinColor) {
            this.skinColor = skinColor;
            return this;
        }

        public Builder setAnimations(List<SkinAnimation> animations) {
            this.animations = animations;
            return this;
        }

        public Builder setPieces(List<SkinPersonaPiece> pieces) {
            this.pieces = pieces;
            return this;
        }

        public Builder setTints(List<SkinPersonaPieceTint> tints) {
            this.tints = tints;
            return this;
        }

        public Builder setCapeHeight(int capeHeight) {
            this.capeHeight = capeHeight;
            return this;
        }

        public Builder setCapeWidth(int capeWidth) {
            this.capeWidth = capeWidth;
            return this;
        }

        public Builder setCapeData(byte[] data) {
            this.capeData = data;
            return this;
        }

        public Builder setPremium(boolean premium) {
            this.premium = premium;
            return this;
        }

        public Builder setTrusted(boolean trusted) {
            this.trusted = trusted;
            return this;
        }

        public Builder setPersona(boolean persona) {
            this.isPersona = persona;
            return this;
        }

        public Skin build() {
            if (this.isPersona) {
                return new Skin(
                        this.skinId,
                        this.fullSkinId,
                        this.playFabId,
                        this.skinResourcePatch,
                        this.geometryData,
                        this.skinHeight,
                        this.skinWidth,
                        this.skinData,
                        this.capeId,
                        this.capeOnClassic,
                        this.capeHeight,
                        this.capeWidth,
                        this.capeData,
                        this.armSize,
                        this.skinColor,
                        this.animationData,
                        this.animations,
                        this.pieces,
                        this.tints,
                        this.premium,
                        this.trusted
                );
            } else {
                return new Skin(
                        this.skinId,
                        this.fullSkinId,
                        this.playFabId,
                        this.geometryData,
                        this.armSize,
                        this.skinHeight,
                        this.skinWidth,
                        this.skinData,
                        this.capeId,
                        this.capeOnClassic,
                        this.capeHeight,
                        this.capeWidth,
                        this.capeData,
                        this.premium,
                        this.trusted
                );
            }
        }

    }

}

