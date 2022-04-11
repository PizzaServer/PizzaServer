package io.github.pizzaserver.api.plugin;

import io.github.pizzaserver.api.Server;

import java.io.File;

public class PluginData {

    private File file;
    private PluginManifest manifest;
    private Server server;

    private Plugin plugin;

    public PluginData(File file, PluginManifest manifest, Server server, Plugin plugin) {
        this.file = file;
        this.manifest = manifest;
        this.server = server;
        this.plugin = plugin;
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

    public Plugin getPlugin() {
        return this.plugin;
    }
}
