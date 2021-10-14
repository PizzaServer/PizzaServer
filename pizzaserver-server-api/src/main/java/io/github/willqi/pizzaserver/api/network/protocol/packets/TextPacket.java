package io.github.willqi.pizzaserver.api.network.protocol.packets;

import java.util.Collections;
import java.util.List;

/**
 * Sent by the server and client to send/display text messages to one another.
 */
public class TextPacket extends BaseBedrockPacket {

    public static final int ID = 0x09;

    private TextType type;
    private boolean requiresTranslation;
    private String message;

    private String sourceName = "";
    private List<String> parameters = Collections.emptyList();

    private String xuid = "";
    private String platformChatId = "";


    public TextPacket() {
        super(ID);
    }

    public TextType getType() {
        return this.type;
    }

    public void setType(TextType type) {
        this.type = type;
    }

    public boolean requiresTranslation() {
        return this.requiresTranslation;
    }

    public void setRequiresTranslation(boolean requires) {
        this.requiresTranslation = requires;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public String getXuid() {
        return this.xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public String getPlatformChatId() {
        return this.platformChatId;
    }

    public void setPlatformChatId(String platformChatId) {
        this.platformChatId = platformChatId;
    }


    public enum TextType {
        RAW,
        CHAT,
        TRANSLATION,
        POPUP,
        JUKEBOX_POPUP,
        TIP,
        SYSTEM,
        WHISPER,
        ANNOUNCEMENT,
        OBJECT,
        OBJECT_WHISPER
    }

}
