package io.github.pizzaserver.server.plugin;

import java.util.HashMap;
import java.util.Map;

public class PluginClassLoaderManager {

    private final Map<String, PluginClassLoader> pluginClassLoaders = new HashMap<>();

    public void registerClassLoader(String pluginName, PluginClassLoader classLoader) {
        this.pluginClassLoaders.put(pluginName, classLoader);
    }

    public PluginClassLoader removeClassLoader(String pluginName) {
        return this.pluginClassLoaders.remove(pluginName);
    }

    public Class<?> getPluginClassByName(String name) {
        for (PluginClassLoader loader : this.pluginClassLoaders.values()) {
            Class<?> cachedClass = null;
            try {
                cachedClass = loader.findPluginClass(name);
            } catch (ClassNotFoundException e) {
                //ignore
            }
            if (cachedClass != null) {
                return cachedClass;
            }
        }
        return null;
    }
}
