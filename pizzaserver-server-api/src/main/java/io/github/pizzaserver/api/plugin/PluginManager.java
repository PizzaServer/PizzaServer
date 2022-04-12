package io.github.pizzaserver.api.plugin;

import java.io.File;
import java.util.Map;

public interface PluginManager {

    Map<String, Plugin> getPlugins();

    void loadPlugin(File file, PluginManifest manifest);

    void unloadPlugin(Plugin plugin);

    void enablePlugin(Plugin plugin);

    void disablePlugin(Plugin plugin);

}
