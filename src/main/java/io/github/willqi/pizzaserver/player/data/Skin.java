package io.github.willqi.pizzaserver.player.data;

import com.nukkitx.protocol.bedrock.data.skin.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Skin {

    private final String skinId;
    private final String playFabId;
    private final String skinResourcePatch;
    private final String geometryData;
    private final ImageData skinData;
    private final ImageData capeData;
    private final String capeId;
    private final boolean capeOnClassic;

    private final String animationData;
    private final String armSize;
    private final String skinColor;
    private final List<AnimationData> animations;
    private final List<PersonaPieceData> pieces;
    private final List<PersonaPieceTintData> tints;
    private final boolean isPersona;

    private final boolean premium;
    private final boolean trusted;

    // Persona skin
    private Skin(
            String skinId,
            String playFabId,
            String skinResourcePatch,
            String geometryData,
            ImageData skinData,
            String capeId,
            boolean capeOnClassic,
            ImageData capeData,
            String armSize,
            String skinColor,
            String animationData,
            List<AnimationData> animations,
            List<PersonaPieceData> pieces,
            List<PersonaPieceTintData> tints,
            boolean premium,
            boolean trusted
    ) {
        this.skinId = skinId;
        this.playFabId = playFabId;
        this.skinResourcePatch = skinResourcePatch;
        this.geometryData = geometryData;
        this.skinData = skinData;
        this.capeId = capeId;
        this.capeOnClassic = capeOnClassic;
        this.capeData = capeData;
        this.premium = premium;
        this.trusted = trusted;

        this.isPersona = true;
        this.armSize = armSize;
        this.skinColor = skinColor;
        this.animationData = animationData;
        this.animations = Collections.unmodifiableList(animations);
        this.pieces = Collections.unmodifiableList(pieces);
        this.tints = Collections.unmodifiableList(tints);
    }

    // Base skin
    private Skin(
            String skinId,
            String playFabId,
            String skinResourcePatch,
            String geometryData,
            ImageData skinData,
            String capeId,
            boolean capeOnClassic,
            ImageData capeData,
            boolean premium,
            boolean trusted
    ) {
        this.skinId = skinId;
        this.playFabId = playFabId;
        this.skinResourcePatch = skinResourcePatch;
        this.geometryData = geometryData;
        this.skinData = skinData;
        this.capeId = capeId;
        this.capeOnClassic = capeOnClassic;
        this.capeData = capeData;
        this.premium = premium;
        this.trusted = trusted;

        this.isPersona = false;
        this.armSize = "";
        this.skinColor = "#O";
        this.animationData = "";
        this.animations = Collections.unmodifiableList(new ArrayList<>());
        this.pieces = Collections.unmodifiableList(new ArrayList<>());
        this.tints = Collections.unmodifiableList(new ArrayList<>());
    }

    public String getSkinId() {
        return this.skinId;
    }

    public String getPlayFabId() {
        return this.playFabId;
    }

    public String getSkinResourcePatch() {
        return this.skinResourcePatch;
    }

    public String getGeometryData() {
        return this.geometryData;
    }

    public ImageData getSkinData() {
        return this.skinData;
    }

    public ImageData getCapeData() {
        return this.capeData;
    }

    public String getCapeId() {
        return this.capeId;
    }

    public boolean isCapeOnClassic() {
        return this.capeOnClassic;
    }

    public String getAnimationData() {
        return this.animationData;
    }

    public String getArmSize() {
        return this.armSize;
    }

    public String getSkinColor() {
        return this.skinColor;
    }

    public List<AnimationData> getAnimations() {
        return this.animations;
    }

    public List<PersonaPieceData> getPieces() {
        return this.pieces;
    }

    public List<PersonaPieceTintData> getTints() {
        return this.tints;
    }

    public boolean isPersona() {
        return this.isPersona;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public boolean isTrusted() {
        return this.trusted;
    }

    public SerializedSkin serialize() {
        return null;
    }

    @Override
    public String toString() {
        if (this.isPersona) {
            return "Skin(skinId=" + this.skinId + ", playFabId=" + this.playFabId + ", armSize=" + this.armSize + ", skinColor=" + this.skinColor + " capeId=" + this.capeId + ", premium=" + this.premium + ", trusted=" + this.trusted + ")";
        } else {
            return "Skin(skinId=" + this.skinId + ", playFabId=" + this.playFabId + ", capeId=" + this.capeId + ", premium=" + this.premium + ", trusted=" + this.trusted + ")";
        }
    }

    @Override
    public int hashCode() {
        return this.skinId.hashCode() +
                this.playFabId.hashCode() +
                this.skinResourcePatch.hashCode() +
                this.geometryData.hashCode() +
                this.skinData.hashCode() +
                this.capeData.hashCode() +
                this.capeId.hashCode() +
                (this.capeOnClassic ? 1 : 0) +
                this.animationData.hashCode() +
                this.armSize.hashCode() +
                this.skinColor.hashCode() +
                this.animations.hashCode() +
                this.pieces.hashCode() +
                this.tints.hashCode() +
                (this.isPersona ? 1 : 0) +
                (this.premium ? 1 : 0) +
                (this.trusted ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Skin) {
            Skin skin = (Skin)obj;
            return this.skinId.equals(skin.getSkinId()) &&
                    this.playFabId.equals(skin.getPlayFabId()) &&
                    this.skinResourcePatch.equals(skin.getSkinResourcePatch()) &&
                    this.geometryData.equals(skin.getGeometryData()) &&
                    this.skinData.equals(skin.getSkinData()) &&
                    this.capeData.equals(skin.getCapeData()) &&
                    this.capeId.equals(skin.getCapeId()) &&
                    this.capeOnClassic == skin.isCapeOnClassic() &&
                    this.animationData.equals(skin.getAnimationData()) &&
                    this.armSize.equals(skin.getArmSize()) &&
                    this.skinColor.equals(skin.getSkinColor()) &&
                    this.animations.equals(skin.getAnimations()) &&
                    this.pieces.equals(skin.getPieces()) &&
                    this.tints.equals(skin.getTints()) &&
                    this.isPersona == skin.isPersona &&
                    this.premium == skin.premium &&
                    this.trusted == skin.trusted;
        } else {
            return false;
        }
    }

    public static class Builder {

        private String skinId;
        private String playFabId;
        private String skinResourcePatch;
        private String geometryData;
        private ImageData skinData;
        private ImageData capeData;
        private String capeId;
        private boolean capeOnClassic;

        private String animationData;
        private String armSize;
        private String skinColor;
        private List<AnimationData> animations;
        private List<PersonaPieceData> pieces = new ArrayList<>();
        private List<PersonaPieceTintData> tints = new ArrayList<>();
        private boolean isPersona;

        private boolean premium;
        private boolean trusted;

        public Builder setSkinId(String skinId) {
            this.skinId = skinId;
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

        public Builder setSkinData(ImageData data) {
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

        public Builder setSkinColor(String skinColor) {
            this.skinColor = skinColor;
            return this;
        }

        public Builder setAnimations(List<AnimationData> animations) {
            this.animations = animations;
            return this;
        }

        public Builder setPieces(List<PersonaPieceData> pieces) {
            this.pieces = pieces;
            return this;
        }

        public Builder setTints(List<PersonaPieceTintData> tints) {
            this.tints = tints;
            return this;
        }

        public Builder setCapeData(ImageData data) {
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

        public Builder isPersona(boolean persona) {
            this.isPersona = persona;
            return this;
        }

        public Skin build() {
            if (this.isPersona) {
                return new Skin(
                        this.skinId,
                        this.playFabId,
                        this.geometryData,
                        this.armSize,
                        this.skinData,
                        this.capeId,
                        this.capeOnClassic,
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
                        this.playFabId,
                        this.geometryData,
                        this.armSize,
                        this.skinData,
                        this.capeId,
                        this.capeOnClassic,
                        this.capeData,
                        this.premium,
                        this.trusted
                );
            }
        }

    }

}

