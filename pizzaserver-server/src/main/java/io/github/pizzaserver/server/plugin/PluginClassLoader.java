package io.github.pizzaserver.server.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {

    PluginClassLoaderManager manager;
    String pluginName;

    public PluginClassLoader(String pluginName, PluginClassLoaderManager manager, ClassLoader parent, File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
        this.manager = manager;
        this.manager.registerClassLoader(pluginName, this);
    }

    //This method is only used internally when the jvm loads classes in the plugin of this loader, contained in other plugins
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return this.findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("io.github.pizzaserver.")) {
            // short-circuit loading server classes, as this will just waste time looking for a server class in this plugin
            throw new ClassNotFoundException(name);
        }
        Class<?> result = super.findLoadedClass(name);

        if (result == null) {
            try {
                result = super.findClass(name);
            } catch (ClassNotFoundException ex) {
                if (checkGlobal) {
                    result = this.manager.getPluginClassByName(name);
                } else {
                    throw ex;
                }
            }
        }

        return result;
    }

    // This method can be used by other plugin class loaders to find classes contained in this plugin
    public Class<?> findPluginClass(String name) throws ClassNotFoundException {
        return this.findClass(name, false);
    }

    @Override
    public void close() throws IOException {
        this.manager.removeClassLoader(this.pluginName);

        super.close();
    }
}
