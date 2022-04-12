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

    private final Map<String, Plugin> pluginMap = new HashMap<>();

    private final PluginClassLoaderManager classLoaderManager = new PluginClassLoaderManager();


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

        plugin:
        for (String name : toLoad) {
            Tuple<File, PluginManifest> tuple = pluginList.get(name);
            File file = tuple.getFirst();
            PluginManifest manifest = tuple.getSecond();

            for (PluginManifest.PluginDependency dependency : manifest.getDependencies()) {
                if (!dependency.isOptional()) {
                    if (!this.getPlugins().containsKey(dependency.getName())) {
                        //TODO add plugin dependency version check here, and error if the loaded version does not match
                        this.server.getLogger().error("Failed to load plugin '" + manifest.getName() + "' v" + manifest.getVersion()
                                + ": Required dependency " + dependency.getName() + ":" + dependency.getVersion() + " was not loaded");
                        continue plugin;
                    }
                } else {
                    //TODO maybe add plugin dependency version check here, and warn if the loaded version does not match
                }
            }

            try {
                this.loadPlugin(file, manifest);
                this.server.getLogger().info("Loaded plugin '" + manifest.getName() + "' v" + manifest.getVersion());
            } catch (Exception e) {
                this.server.getLogger().error("Failed to load plugin '" + manifest.getName() + "' v" + manifest.getVersion(), e);
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

    public void enablePlugins() {
        for (Plugin plugin : this.getPlugins().values()) {
            this.enablePlugin(plugin);
        }
    }

    public void disablePlugins() {
        for (Plugin plugin : this.getPlugins().values()) {
            this.disablePlugin(plugin);
        }
    }

    @Override
    public Map<String, Plugin> getPlugins() {
        return Collections.unmodifiableMap(this.pluginMap);
    }

    @Override
    public void loadPlugin(File file, PluginManifest manifest) {
        PluginClassLoader classLoader;
        try {
            classLoader = new PluginClassLoader(manifest.getName(), this.classLoaderManager, this.getClass().getClassLoader(), file);
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

                PluginData data = new PluginData(file, manifest, this.server);

                Plugin plugin = pluginClass.getConstructor(Server.class, PluginData.class).newInstance(this.server, data);

                this.pluginMap.put(manifest.getName(), plugin);

                plugin.onLoad();
            } catch (ClassCastException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Error whilst initializing main class '" + manifest.getMainClass() + "'. The main class must implement the Plugin interface.", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Plugin does not have a constructor with arguments (Server, PluginData)", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Failed to call plugin constructor", e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find plugin main class '" + manifest.getMainClass() + "' in file '" + file.getName() + "'");
        }
    }

    @Override
    public void unloadPlugin(Plugin plugin) {
        String name = plugin.getData().getManifest().getName();
        PluginClassLoader loader = this.classLoaderManager.removeClassLoader(name);
        if (loader != null) {
            try {
                loader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.pluginMap.remove(name);
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        PluginData data = plugin.getData();

        if (!data.isEnabled()) {
            data.setEnabled(true);
            plugin.onEnable();
            this.server.getLogger().info("Enabled plugin '" + data.getManifest().getName() + "' v" + data.getManifest().getVersion());
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        PluginData data = plugin.getData();

        if (data.isEnabled()) {
            data.setEnabled(false);

            //TODO remove plugin listeners, scheduled tasks, etc

            plugin.onDisable();
            this.server.getLogger().info("Disabled plugin '" + data.getManifest().getName() + "' v" + data.getManifest().getVersion());
        }
    }
}
