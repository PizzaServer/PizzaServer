package io.github.willqi.pizzaserver.api.network.protocol.packets;

import java.util.HashSet;
import java.util.Set;

/**
 * Sent to the client to notify them of all available entity identiifers.
 */
public class AvailableEntityIdentifiersPacket extends BaseBedrockPacket {

    public static final int ID = 0x77;

    private Set<Entry> entries = new HashSet<>();


    public AvailableEntityIdentifiersPacket() {
        super(ID);
    }

    public Set<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(Set<Entry> entries) {
        this.entries = entries;
    }


    public static class Entry {

        private final String id;
        private final boolean summonable;
        private final boolean spawnEgg;


        public Entry(String id, boolean summonable, boolean spawnEgg) {
            this.id = id;
            this.summonable = summonable;
            this.spawnEgg = spawnEgg;
        }

        public String getId() {
            return this.id;
        }

        public boolean isSummonable() {
            return this.summonable;
        }

        public boolean hasSpawnEgg() {
            return this.spawnEgg;
        }

    }

}
