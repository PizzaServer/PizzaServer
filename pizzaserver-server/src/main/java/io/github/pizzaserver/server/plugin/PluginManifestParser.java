package io.github.pizzaserver.server.plugin;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.pizzaserver.api.plugin.PluginManifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * To allow for shorthands in the plugin manifest, we implement a custom JSON parser. Writing GSON deserializer type adapters makes for too complex code imho. - DasEtwas
 */
public class PluginManifestParser {

    /**
     * Required fields include:
     * <ul>
     *     <li>name</li>
     *     <li>mainClass</li>
     *     <li>version</li>
     *     <li>apiVersion</li>
     * </ul>.
     */
    public static PluginManifest parse(JsonObject manifest) {
        Object nameO = Objects.requireNonNull(manifest.get("name"), "Plugin manifest requires a \"name\" key.");
        Preconditions.checkArgument(nameO instanceof JsonPrimitive primitive && primitive.isString(), "Plugin manifest requires a \"name\" key of type string.");
        String name = ((JsonPrimitive) nameO).getAsString();

        Object mainClassO = Objects.requireNonNull(manifest.get("mainClass"), "Plugin manifest requires a \"mainClass\" key.");
        Preconditions.checkArgument(mainClassO instanceof JsonPrimitive primitive && primitive.isString(), "Plugin manifest requires a \"mainClass\" key of type string.");
        String mainClass = ((JsonPrimitive) mainClassO).getAsString();

        Object versionO = Objects.requireNonNull(manifest.get("version"), "Plugin manifest requires a \"version\" key.");
        Preconditions.checkArgument(versionO instanceof JsonPrimitive primitive && primitive.isString(), "Plugin manifest requires a \"version\" key of type string.");
        String version = ((JsonPrimitive) versionO).getAsString();

        Object apiVersionO = Objects.requireNonNull(manifest.get("apiVersion"), "Plugin manifest requires a \"apiVersion\" key.");
        Preconditions.checkArgument(apiVersionO instanceof JsonPrimitive primitive && primitive.isString(), "Plugin manifest requires a \"apiVersion\" key of type string.");
        String apiVersion = ((JsonPrimitive) apiVersionO).getAsString();

        List<PluginManifest.PluginDependency> dependencies = new ArrayList<>();
        if (manifest.has("dependencies")) {
            JsonArray depArr = manifest.getAsJsonArray("dependencies");
            if (depArr != null) {
                for (int i = 0; i < depArr.size(); i++) {
                    try {
                        dependencies.add(parseDependency(depArr.get(i)));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse dependency at index " + i, e);
                    }
                }
            } else {
                throw new IllegalArgumentException("Plugin manifest has a \"dependency\" value of invalid type. Required type: array");
            }
        }

        PluginManifest.Metadata metadata = null;
        if (manifest.has("metadata")) {
            JsonElement element = manifest.get("metadata");
            if (element instanceof JsonObject metadataO) {
                try {
                    metadata = parseMetadata(metadataO);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse plugin metadata", e);
                }
            } else {
                throw new IllegalArgumentException("Plugin manifest has a \"metadata\" value of invalid type. Required type: object");
            }
        }

        return new PluginManifest(name, mainClass, version, apiVersion, dependencies.toArray(PluginManifest.PluginDependency[]::new), metadata);
    }

    public static PluginManifest.PluginDependency parseDependency(JsonElement dependency) {
        if (dependency.isJsonPrimitive() && dependency instanceof JsonPrimitive dependencyPrimitive && dependencyPrimitive.isString()) {
            // short form
            /*
             * examples:
             * plugin dependency: "ExamplePlugin"
             * or
             * plugin dependency with version requirement: "ExamplePlugin:0.3.4"
             */

            String dependencyS = dependencyPrimitive.getAsString();
            String[] parts = dependencyS.split(":");

            return switch (parts.length) {
                case 1 -> new PluginManifest.PluginDependency(false, null, parts[0]);
                case 2 -> new PluginManifest.PluginDependency(false, parts[1], parts[0]);
                default -> throw new IllegalArgumentException("Failed to parse plugin dependency for \"" + dependencyS + "\"");
            };

        } else if (dependency instanceof JsonObject dependencyObj) {
            // object form
            /*
             * examples:
             * plugin dependency with version requirement:
             *  {
             *      "name": "ExamplePlugin",
             *      "version": "0.3.4"
             *  }
             * optional plugin dependency with version requirement:
             *  {
             *      "name": "ExamplePlugin",
             *      "version": "0.3.4",
             *      "optional": true
             *  }
             */
            Object nameO = Objects.requireNonNull(dependencyObj.get("name"), "Plugin dependency requires a \"name\" key.");
            Preconditions.checkArgument(nameO instanceof JsonPrimitive primitive && primitive.isString(), "Plugin dependency requires a \"name\" key of type string.");
            String name = ((JsonPrimitive) nameO).getAsString();

            Object versionO = Objects.requireNonNull(dependencyObj.get("version"), "Plugin dependency for '" + name + "' requires a \"version\" key.");
            Preconditions.checkArgument(versionO instanceof JsonPrimitive primitive && primitive.isString(), "Plugin dependency for '" + name + "' requires a \"version\" key of type string.");
            String version = ((JsonPrimitive) versionO).getAsString();

            boolean optional = false;

            if (dependencyObj.has("optional")) {
                if (dependencyObj.get("optional") instanceof JsonPrimitive optionalPrimitive && optionalPrimitive.isBoolean()) {
                    optional = optionalPrimitive.getAsBoolean();
                } else {
                    throw new IllegalArgumentException("Plugin dependency for '" + name + "' has an \"optional\" value of invalid type. Expected: boolean");
                }
            }

            return new PluginManifest.PluginDependency(optional, version, name);
        } else {
            throw new IllegalArgumentException("Plugin dependency must be either a string value or an object.");
        }
    }

    /**
     * All metadata fields are optional.
     */
    public static PluginManifest.Metadata parseMetadata(JsonObject metadata) {
        /*
         * examples:
         *  {
         *      "author": "Example Author",
         *      OR
         *      "authors": ["Author 1", Author 2"],
         *
         *
         *      "license": "MIT/Apache 2.0",
         *
         *
         *      "website": "https://plugin.plugin/",
         *
         *
         *      "description": "Test hello hello"
         *  }
         */
        List<String> authors = new ArrayList<>();
        if (metadata.get("authors") instanceof JsonArray authorsArr) {
            for (int i = 0; i < authorsArr.size(); i++) {
                try {
                    authors.add(authorsArr.get(i).getAsString());
                } catch (Exception e) {
                    throw new IllegalArgumentException("\"authors\" array has invalid value at index " + i);
                }
            }
        } else if (metadata.get("author") instanceof JsonPrimitive authorPrimitive && authorPrimitive.isString()) {
            authors.add(authorPrimitive.getAsString());
        } else if (metadata.has("author") || metadata.has("authors")) {
            throw new IllegalArgumentException("Plugin metadata has an \"author\" or \"authors\" field of invalid format. Expected either an \"author\" string or an \"authors\" string array.");
        }
        String license = null;
        if (metadata.has("license")) {
            if (metadata.get("license") instanceof JsonPrimitive licenseP && licenseP.isString()) {
                license = licenseP.getAsString();
            } else {
                throw new IllegalArgumentException("Plugin metadata has a \"license\" value of invalid type. Expected: string");
            }
        }
        String website = null;
        if (metadata.has("website")) {
            if (metadata.get("website") instanceof JsonPrimitive websiteP && websiteP.isString()) {
                website = websiteP.getAsString();
            } else {
                throw new IllegalArgumentException("Plugin metadata has a \"website\" value of invalid type. Expected: string");
            }
        }
        String description = null;
        if (metadata.has("description")) {
            if (metadata.get("description") instanceof JsonPrimitive descriptionP && descriptionP.isString()) {
                description = descriptionP.getAsString();
            } else {
                throw new IllegalArgumentException("Plugin metadata has a \"description\" value of invalid type. Expected: string");
            }
        }

        if (license == null && website == null && description == null && authors.isEmpty()) {
            return null;
        } else {
            return new PluginManifest.Metadata(authors.toArray(String[]::new), license, website, description);
        }
    }

}
