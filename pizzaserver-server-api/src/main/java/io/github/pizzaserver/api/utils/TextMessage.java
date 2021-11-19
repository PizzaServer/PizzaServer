package io.github.pizzaserver.api.utils;

import com.nukkitx.protocol.bedrock.packet.TextPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextMessage {

    private final TextPacket.Type type;
    private final String message;
    private final boolean requiresTranslation;

    private final String sourceName;
    private final List<String> parameters;

    private final String xuid;
    private final String platformChatId;

    private TextMessage(TextPacket.Type textType,
                        String message,
                        boolean requiresTranslation,
                        String sourceName,
                        List<String> parameters,
                        String xuid,
                        String platformChatId) {
        this.type = textType;
        this.message = message;
        this.requiresTranslation = requiresTranslation;
        this.sourceName = sourceName;
        this.parameters = parameters;
        this.xuid = xuid;
        this.platformChatId = platformChatId;
    }

    public TextPacket.Type getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean requiresTranslation() {
        return this.requiresTranslation;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public List<String> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    public String getXuid() {
        return this.xuid;
    }

    public String getPlatformChatId() {
        return this.platformChatId;
    }


    public static class Builder {

        private TextPacket.Type type;
        private boolean requiresTranslation;
        private String message = "";

        private String sourceName = "";
        private List<String> parameters = new ArrayList<>();

        private String xuid = "";
        private String platformChatId = "";

        public Builder setType(TextPacket.Type type) {
            this.type = type;
            return this;
        }

        public Builder setTranslationRequired(boolean requiresTranslation) {
            this.requiresTranslation = requiresTranslation;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder appendText(String message) {
            this.message += message;
            return this;
        }

        public Builder setSourceName(String sourceName) {
            this.sourceName = sourceName;
            return this;
        }

        public Builder setParameters(List<String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder addParameter(String parameter) {
            this.parameters.add(parameter);
            return this;
        }

        public Builder setXUID(String xuid) {
            this.xuid = xuid;
            return this;
        }

        public Builder setPlatformChatId(String platformChatId) {
            this.platformChatId = platformChatId;
            return this;
        }

        public TextMessage build() {
            return new TextMessage(this.type,
                    this.message,
                    this.requiresTranslation,
                    this.sourceName,
                    this.parameters,
                    this.xuid,
                    this.platformChatId);
        }

    }

}
