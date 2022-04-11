package io.github.pizzaserver.api.plugin;

import io.github.pizzaserver.api.Server;

public interface Plugin {

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

    default PluginManifest getManifest() {
        return this.getServer().getPluginManager().getData(this).getManifest();
    }

}
