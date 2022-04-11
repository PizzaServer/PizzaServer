package io.github.pizzaserver.server.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginClassLoader extends URLClassLoader {

    PluginClassesCache pluginClassesCache;
    String pluginName;

    private final Map<String, Class<?>> pluginClasses = new HashMap<>();

    public PluginClassLoader(String pluginName, PluginClassesCache pluginClassesCache, ClassLoader parent, File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
        this.pluginClassesCache = pluginClassesCache;
        this.pluginClassesCache.registerClassLoader(pluginName, this);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return this.findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean lookupClassCache) throws ClassNotFoundException {
        if (name.startsWith("io.github.pizzaserver.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = this.pluginClasses.get(name);

        if (result == null) {
            if (lookupClassCache) {
                result = this.pluginClassesCache.getCachedPluginClassByName(name);
            }

            if (result == null) {
                result = super.findClass(name);

                if (result != null) {
                    this.pluginClassesCache.addCachedPluginClass(name, result);
                }
            }

            this.pluginClasses.put(name, result);
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        for (String name : this.pluginClasses.keySet()) {
            this.pluginClassesCache.removeCachedPluginClass(name);
        }
        this.pluginClassesCache.unregisterClassLoader(this.pluginName);

        super.close();
    }
}
