package io.github.pizzaserver.server.plugin;

import java.util.HashMap;
import java.util.Map;

public class PluginClassesCache {

    private final Map<String, Class<?>> pluginClassCache = new HashMap<>();
    private final Map<String, PluginClassLoader> pluginClassLoaders = new HashMap<>();

    public void registerClassLoader(String pluginName, PluginClassLoader classLoader) {
        this.pluginClassLoaders.put(pluginName, classLoader);
    }

    public void unregisterClassLoader(String pluginName) {
        this.pluginClassLoaders.remove(pluginName);
    }

    public Class<?> getCachedPluginClassByName(String name) {
        Class<?> cachedClass = this.pluginClassCache.get(name);

        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (PluginClassLoader loader : this.pluginClassLoaders.values()) {
                try {
                    // false here prevents infinite recursion
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException e) {
                    //ignore
                }

                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

    public void addCachedPluginClass(String path, Class<?> clazz) {
        this.pluginClassCache.put(path, clazz);
    }

    public void removeCachedPluginClass(String path) {
        this.pluginClassCache.remove(path);
    }
}
