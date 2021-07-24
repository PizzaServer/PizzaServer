package io.github.willqi.pizzaserver.api.player.skin;

import java.util.List;

/**
 * Represents the skin of a player entity
 */
public interface Skin {

    String getSkinId();

    void setSkinId(String skinId);

    String getPlayFabId();

    void setPlayFabId(String playFabId);

    String getSkinResourcePatch();

    void setSkinResourcePatch(String resourcePatch);

    String getGeometryData();

    void setGeometryData(String geometryData);

    int getSkinHeight();

    void setSkinHeight(int skinHeight);

    int getSkinWidth();

    void setSkinWidth(int skinWidth);

    byte[] getSkinData();

    void setSkinData(byte[] data);

    int getCapeHeight();

    void setCapeHeight(int capeHeight);

    int getCapeWidth();

    void setCapeWidth(int capeWidth);

    byte[] getCapeData();

    void setCapeData(byte[] capeData);

    String getCapeId();

    void setCapeId(String capeId);

    boolean isCapeOnClassic();

    void setCapeOnClassic(boolean isOnClassic);

    String getAnimationData();

    void setAnimationData(String data);

    String getArmSize();

    void setArmSize(String armSize);

    String getSkinColour();

    void setSkinColour(String skinColour);

    List<SkinAnimation> getAnimations();

    void setAnimations(List<SkinAnimation> animations);

    List<SkinPersonaPiece> getPieces();

    void setPieces(List<SkinPersonaPiece> pieces);

    List<SkinPersonaPieceTint> getTints();

    void setTints(List<SkinPersonaPieceTint> tints);

    boolean isPersona();

    void setPersona(boolean isPersona);

    boolean isPremium();

    void setPremium(boolean isPremium);

    boolean isTrusted();

    void setTrusted(boolean trusted);

}
