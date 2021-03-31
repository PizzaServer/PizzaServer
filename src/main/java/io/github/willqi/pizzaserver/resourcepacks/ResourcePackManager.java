package io.github.willqi.pizzaserver.resourcepacks;

import io.github.willqi.pizzaserver.Server;

import java.util.HashSet;
import java.util.Set;

public class ResourcePackManager {

    private boolean required;
    private Server server;

    public ResourcePackManager(Server server) {
        this.server = server;
    }

    public boolean isPacksRequired() {
        return this.required;
    }

    public Set<ResourcePack> getPacks() {
        return new HashSet<>();
    }

    public void setPacksRequired(boolean required) {
        this.required = required;
    }

}
