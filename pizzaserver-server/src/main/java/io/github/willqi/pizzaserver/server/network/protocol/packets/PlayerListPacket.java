package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.commons.utils.Check;

import java.util.List;
import java.util.UUID;

/**
 * Sent to update the player list in-game
 * This is also required to be sent before a AddPlayer packet
 * or else the player entity will not spawn.
 */
public class PlayerListPacket extends BaseBedrockPacket {

    public static final int ID = 0x3F;

    private ActionType actionType;
    private List<Entry> entries;


    public PlayerListPacket() {
        super(ID);
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }


    public enum ActionType {
        ADD,
        REMOVE
    }

    public static class Entry {

        private final UUID uuid;
        private final long entityRuntimeId;
        private final String username;
        private final String xuid;
        private final String platformChatId;
        private final Device device;
        private final Skin skin;

        // Edu only properties
        private final boolean teacher;
        private final boolean host;

        private Entry(UUID uuid,
                     long entityRuntimeId,
                     String username,
                     String xuid,
                     String platformChatId,
                     Device device,
                     Skin skin,
                     boolean teacher,
                     boolean host) {
            this.uuid = uuid;
            this.entityRuntimeId = entityRuntimeId;
            this.username = username;
            this.xuid = xuid;
            this.platformChatId = platformChatId;
            this.device = device;
            this.skin = skin;
            this.teacher = teacher;
            this.host = host;
        }

        public UUID getUUID() {
            return this.uuid;
        }

        public long getEntityRuntimeId() {
            return this.entityRuntimeId;
        }

        public String getUsername() {
            return this.username;
        }

        public String getXUID() {
            return this.xuid;
        }

        /**
         * Used on specific platforms to limit who can see messages sent by this player
         * @return platform chat id
         */
        public String getPlatformChatId() {
            return this.platformChatId;
        }

        /**
         * Get the device of the player
         * @return player's device
         */
        public Device getDevice() {
            return this.device;
        }

        public Skin getSkin() {
            return this.skin;
        }

        public boolean isTeacher() {
            return this.teacher;
        }

        public boolean isHost() {
            return this.host;
        }


        public static class Builder {

            private UUID uuid;
            private long entityRuntimeId;
            private String username;
            private String xuid;
            private String platformChatId = "";
            private Device device;
            private Skin skin;

            // Edu only properties
            private boolean teacher;
            private boolean host;


            public Builder setUUID(UUID uuid) {
                this.uuid = uuid;
                return this;
            }

            public Builder setEntityRuntimeId(long entityRuntimeId) {
                this.entityRuntimeId = entityRuntimeId;
                return this;
            }

            public Builder setUsername(String username) {
                this.username = username;
                return this;
            }

            public Builder setXUID(String xuid) {
                this.xuid = xuid;
                return this;
            }

            public Builder setPlatformChatId(String  platformChatId) {
                this.platformChatId = platformChatId;
                return this;
            }

            public Builder setDevice(Device device) {
                this.device = device;
                return this;
            }

            public Builder setSkin(Skin skin) {
                this.skin = skin;
                return this;
            }

            public Builder setIsTeacher(boolean isTeacher) {
                this.teacher = isTeacher;
                return this;
            }

            public Builder setIsHost(boolean isHost) {
                this.host = isHost;
                return this;
            }

            public Entry build() {
                return new Entry(
                        Check.nullParam(this.uuid, "uuid"),
                        this.entityRuntimeId,
                        Check.nullParam(this.username, "username"),
                        Check.nullParam(this.xuid, "xuid"),
                        this.platformChatId,
                        this.device,
                        this.skin,
                        this.teacher,
                        this.host
                );
            }



        }


    }

}
