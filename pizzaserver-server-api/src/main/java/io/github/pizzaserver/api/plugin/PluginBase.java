package io.github.pizzaserver.api.plugin;

import io.github.pizzaserver.api.Server;

/**
 * Minimal plugin class to comply with the plugin loading interface.
 * To make a plugin, either {@code extend} this class or implement {@link Plugin}.
 * </p>
 * Note that at least the {@code data.getManifest().getName()} must not change from the original value, as it is used
 * as a key by the server.
 */
public class PluginBase implements Plugin {

    private final Server server;
    private final PluginData data;

    public PluginBase(Server server, PluginData data) {
        this.server = server;
        this.data = data;
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public PluginData getData() {
        return this.data;
    }
}
