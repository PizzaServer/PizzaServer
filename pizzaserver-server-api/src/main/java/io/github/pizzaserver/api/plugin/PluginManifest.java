package io.github.pizzaserver.api.plugin;

import java.util.Arrays;

public class PluginManifest {

    /**
     * Human-readable plugin name.
     */
    private final String name;
    /**
     * Main class path.
     */
    private final String mainClass;
    /**
     * Plugin version, preferably in format {@code major.minor.patch}, e.g. {@code 0.3.4}.
     */
    private final String version;
    /**
     * Required PizzaServer API version.
     */
    private final String apiVersion;

    private final PluginDependency[] dependencies;
    private final Metadata metadata;

    public PluginManifest(String name, String mainClass, String version, String apiVersion, PluginDependency[] dependencies, Metadata metadata) {
        this.name = name;
        this.mainClass = mainClass;
        this.version = version;
        this.apiVersion = apiVersion;
        this.dependencies = dependencies;
        this.metadata = metadata;
    }

    public static class Metadata {
        private final String[] authors;
        private final String license;
        private final String website;
        private final String description;

        public Metadata(String[] authors, String license, String website, String description) {
            this.authors = authors;
            this.license = license;
            this.website = website;
            this.description = description;
        }

        @Override
        public String toString() {
            return "Metadata{"
                    + "authors=" + Arrays.toString(this.authors)
                    + ", license=" + this.license
                    + ", website=" + this.website
                    + ", description=" + this.description
                    + '}';
        }
    }

    public static class PluginDependency {
        /**
         * The dependent plugin does not fail to load if this dependency is missing while {@code optional == true}.
         */
        private final boolean optional;
        /**
         * Version requirement for the dependency. If this does not match, it will fail to load.
         */
        private final String version;
        private final String name;

        public PluginDependency(boolean optional, String version, String name) {
            this.optional = optional;
            this.version = version;
            this.name = name;
        }

        @Override
        public String toString() {
            return "PluginDependency{"
                    + "optional=" + this.optional
                    + ", version=" + this.version
                    + ", name=" + this.name
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "PluginManifest{"
                + "name=" + this.name
                + ", mainClass=" + this.mainClass
                + ", version=" + this.version
                + ", apiVersion=" + this.apiVersion
                + ", dependencies=" + Arrays.toString(this.dependencies)
                + ", metadata=" + this.metadata
                + '}';
    }
}
