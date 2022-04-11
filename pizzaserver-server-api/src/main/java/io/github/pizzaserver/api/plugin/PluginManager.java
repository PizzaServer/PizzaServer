package io.github.pizzaserver.api.plugin;

import java.io.File;
import java.util.Map;

public interface PluginManager {

    Map<String, PluginData> getPlugins();

    PluginData getData(Plugin plugin);

    Plugin loadPlugin(File file, PluginManifest manifest);

    void enablePlugin(Plugin plugin);

    void disablePlugin(Plugin plugin);

}
