package io.github.willqi.pizzaserver.api.network.protocol.packets;

/**
 * Sent to render a bossbar.
 */
public class BossEventPacket extends BaseBedrockPacket {

    public static final int ID = 0x4a;

    private Type type;
    private long entityRuntimeId;
    private long playerRuntimeId;
    private String title;
    private float percentage;
    private int darkenSkyValue;
    private int colour;
    private int overlay;


    public BossEventPacket() {
        super(ID);
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public long getPlayerRuntimeId() {
        return this.playerRuntimeId;
    }

    public void setPlayerRuntimeId(long playerRuntimeId) {
        this.playerRuntimeId = playerRuntimeId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPercentage() {
        return this.percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getDarkenSkyValue() {
        return this.darkenSkyValue;
    }

    public void setDarkenSkyValue(int darkenSkyValue) {
        this.darkenSkyValue = darkenSkyValue;
    }

    public int getColour() {
        return this.colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public int getOverlay() {
        return this.overlay;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public enum Type {
        SHOW,

        /**
         * Sent by the client.
         */
        REGISTER_PLAYER,

        HIDE,

        /**
         * Sent by the client.
         */
        UNREGISTER_PLAYER,

        CHANGE_HEALTH_PERCENTAGE,

        CHANGE_TITLE,

        CHANGE_APPEARANCE,

        CHANGE_TEXTURE
    }

}
