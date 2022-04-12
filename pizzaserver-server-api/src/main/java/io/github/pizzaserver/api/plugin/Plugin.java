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

    Server getServer();

    PluginData getData();

    default PluginManifest getManifest() {
        return this.getData().getManifest();
    }

}
