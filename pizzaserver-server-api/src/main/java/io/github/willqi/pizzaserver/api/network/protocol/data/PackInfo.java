package io.github.willqi.pizzaserver.api.network.protocol.data;

import java.util.UUID;

public class PackInfo {

    private final UUID uuid;
    private final String version;

    public PackInfo(UUID uuid, String version) {
        this.uuid = uuid;
        this.version = version;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getVersion() {
        return this.version;
    }

}