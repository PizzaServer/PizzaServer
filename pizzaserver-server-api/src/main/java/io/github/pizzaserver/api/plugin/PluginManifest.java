package io.github.pizzaserver.api.plugin;

public class PluginManifest {

    private String name;
    private String mainClass;
    private String version;
    private String apiVersion;
    private String[] authors;

    private String[] dependencies;

    public String getName() {
        return this.name;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public String getVersion() {
        return this.version;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public String[] getAuthors() {
        return this.authors;
    }

    public String[] getDependencies() {
        return this.dependencies;
    }
}
