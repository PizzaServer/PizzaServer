package io.github.pizzaserver.api.plugin;

import io.github.pizzaserver.api.Server;

import java.io.File;

/**
 * Data which the plugin keeps for the server.
 * This includes the {@link PluginManifest} which is parsed from {@code plugin.json} or {@code pizza.json}.
 */
public class PluginData {

    private File file;
    private PluginManifest manifest;
    private Server server;
    boolean enabled;

    public PluginData(File file, PluginManifest manifest, Server server) {
        this.file = file;
        this.manifest = manifest;
        this.server = server;
    }

    public File getFile() {
        return this.file;
    }

    public PluginManifest getManifest() {
        return this.manifest;
    }

    public Server getServer() {
        return this.server;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        return this.manifest.getName().hashCode() ^ this.manifest.getVersion().hashCode();
    }
}
