package io.github.pizzaserver.api.player;

import io.github.pizzaserver.api.player.data.Device;
import io.github.pizzaserver.api.player.data.Skin;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.commons.utils.NumberUtils;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents the player list a player currently has.
 */
public interface PlayerList {

    /**
     * Add a new entry to the player's player list.
     * @param entry player list entry
     */
    void addEntry(Entry entry);

    /**
     * Add multiple entries to the player's player list.
     * @param entries player list entries
     */
    void addEntries(Collection<Entry> entries);

    /**
     * Remove a entry from the player's player list.
     * @param entry player list entry
     */
    void removeEntry(Entry entry);

    /**
     * Remove multiple entries from the player's player list.
     * @param entries player list entries
     */
    void removeEntries(Collection<Entry> entries);

    /**
     * Retrieve the entries a player is currently shown.
     * @return player list entries
     */
    List<Entry> getEntries();


    class Entry {

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
         * Used on specific platforms to limit who can see messages sent by this player.
         * @return platform chat id
         */
        public String getPlatformChatId() {
            return this.platformChatId;
        }

        /**
         * Get the device of the player.
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

        @Override
        public int hashCode() {
            return (43 * this.getUUID().hashCode())
                    + (int) (43 * this.getEntityRuntimeId())
                    + (43 * this.getUsername().hashCode())
                    + (43 * this.getXUID().hashCode())
                    + (43 * this.getPlatformChatId().hashCode())
                    + (43 * this.getDevice().hashCode())
                    + (43 * this.getSkin().hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PlayerList.Entry) {
                PlayerList.Entry otherEntry = (PlayerList.Entry) obj;
                return otherEntry.getUUID().equals(this.getUUID())
                        && NumberUtils.isNearlyEqual(otherEntry.getEntityRuntimeId(), this.getEntityRuntimeId())
                        && otherEntry.getUsername().equals(this.getUsername())
                        && otherEntry.getXUID().equals(this.getXUID())
                        && otherEntry.getPlatformChatId().equals(this.getPlatformChatId())
                        && otherEntry.getDevice().equals(this.getDevice())
                        && otherEntry.getSkin().equals(this.getSkin());
            }
            return false;
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
                        Check.notNull(this.uuid, "uuid"),
                        this.entityRuntimeId,
                        Check.notNull(this.username, "username"),
                        Check.notNull(this.xuid, "xuid"),
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
