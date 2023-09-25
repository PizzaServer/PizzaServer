package io.github.pizzaserver.api.plugin;

import io.github.pizzaserver.api.Server;

public interface Plugin {

    /**
     * Callback called after this class is constructed.
     */
    default void onLoad() {
    }

    /**
     * Callback called after this plugin is enabled.
     */
    default void onEnable() {
    }

    /**
     * Callback called after this plugin is disabled.
     */
    default void onDisable() {
    }

    /**
     * Returns the plugin's {@link Server} reference provided to it at construction.
     */
    Server getServer();

    /**
     * Returns the plugin's {@link PluginData} provided to it at construction.
     */
    PluginData getData();

    default PluginManifest getManifest() {
        return this.getData().getManifest();
    }

}
