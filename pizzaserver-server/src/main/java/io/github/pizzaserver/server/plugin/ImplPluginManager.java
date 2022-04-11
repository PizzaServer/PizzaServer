package io.github.pizzaserver.server.plugin;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.plugin.Plugin;
import io.github.pizzaserver.api.plugin.PluginData;
import io.github.pizzaserver.api.plugin.PluginManager;
import io.github.pizzaserver.api.plugin.PluginManifest;
import io.github.pizzaserver.commons.utils.Tuple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ImplPluginManager implements PluginManager {

    private final Gson pluginManifestGson;

    private final Server server;

    private final Map<String, PluginData> byName = new HashMap<>();
    private final Map<Plugin, PluginData> byInstance = new HashMap<>();

    private final PluginClassesCache pluginClassesCache = new PluginClassesCache();


    public ImplPluginManager(Server server) {
        this.server = server;
        this.pluginManifestGson = new Gson();
    }

    public void loadPlugins(File pluginsDirectory) {
        Preconditions.checkArgument(pluginsDirectory.isDirectory());

        Map<String, Tuple<File, PluginManifest>> pluginList = new HashMap<>();

        for (File pluginFile : pluginsDirectory.listFiles((file, name) -> name.endsWith(".jar"))) {
            PluginManifest pluginManifest = null;


            try {
                pluginManifest = this.getPluginManifest(pluginFile);
            } catch (Exception e) {
                this.server.getLogger().error("Failed to load plugin file '" + pluginFile.getName() + "': Failed to load plugin manifest", e);
            }

            if (pluginManifest == null) {
                // null means not a plugin
                continue;
            }

            Tuple<File, PluginManifest> overwritten = pluginList.put(pluginManifest.getName(), new Tuple<>(pluginFile, pluginManifest));
            if (overwritten != null) {
                this.server.getLogger().info("Two versions of the same plugin '" + pluginManifest.getName() + "' are present: "
                        + overwritten.getSecond().getVersion() + " will be overridden by " + pluginManifest.getVersion() + ".");
            }
        }

        DependencyGraph dependencyGraph = new DependencyGraph();

        for (Tuple<File, PluginManifest> tuple : pluginList.values()) {
            dependencyGraph.queue(
                    tuple.getSecond().getName(),
                    Arrays.stream(tuple.getSecond().getDependencies()).map(PluginManifest.PluginDependency::getName).toList(),
                    Arrays.stream(tuple.getSecond().getDependencies()).filter(PluginManifest.PluginDependency::isOptional).map(PluginManifest.PluginDependency::getName).toList()
            );
        }

        List<String> toLoad = dependencyGraph.finish();

        for (String name : toLoad) {
            Tuple<File, PluginManifest> tuple = pluginList.get(name);

            try {
                this.loadPlugin(tuple.getFirst(), tuple.getSecond());
                this.server.getLogger().info("Loaded plugin '" + tuple.getSecond().getName() + "' v" + tuple.getSecond().getVersion());
            } catch (Exception e) {
                this.server.getLogger().error("Failed to load plugin '" + tuple.getSecond().getName() + "' v" + tuple.getSecond().getVersion(), e);
            }
        }
    }

    public PluginManifest getPluginManifest(File file) {
        try (JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("plugin.json");
            if (entry == null) {
                entry = jar.getJarEntry("pizza.json");
                if (entry == null) {
                    return null;
                }
            }
            try (InputStream stream = jar.getInputStream(entry)) {
                return PluginManifestParser.parse(this.pluginManifestGson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class));
            } finally {
                jar.close();
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Map<String, PluginData> getPlugins() {
        return Collections.unmodifiableMap(this.byName);
    }

    @Override
    public PluginData getData(Plugin plugin) {
        return this.byInstance.get(plugin);
    }

    @Override
    public Plugin loadPlugin(File file, PluginManifest manifest) {
        PluginClassLoader classLoader;
        try {
            classLoader = new PluginClassLoader(manifest.getName(), this.pluginClassesCache, this.getClass().getClassLoader(), file);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load plugin", e);
        }
        try {
            Class<?> mainClass = classLoader.loadClass(manifest.getMainClass());

            if (!Plugin.class.isAssignableFrom(mainClass)) {
                throw new RuntimeException("Plugin main class '" + manifest.getMainClass() + "' does not implement Plugin");
            }

            try {
                Class<Plugin> pluginClass = (Class<Plugin>) mainClass.asSubclass(Plugin.class);

                Plugin plugin = pluginClass.getConstructor(Server.class).newInstance(this.server);

                PluginData data = new PluginData(file, manifest, this.server, plugin);

                this.byInstance.put(plugin, data);
                this.byName.put(manifest.getName(), data);

                return plugin;
            } catch (ClassCastException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Error whilst initializing main class '" + manifest.getMainClass() + "'", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Plugin does not have a constructor with arguments (Server)", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Failed to call plugin constructor", e);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find plugin main class '" + manifest.getMainClass() + "' in file '" + file.getName() + "'");
        }
    }

    @Override
    public void enablePlugin(Plugin plugin) {

    }

    @Override
    public void disablePlugin(Plugin plugin) {

    }

}
