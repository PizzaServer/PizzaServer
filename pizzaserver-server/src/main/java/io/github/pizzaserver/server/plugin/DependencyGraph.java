package io.github.pizzaserver.server.plugin;

import com.google.common.base.Preconditions;

import java.util.*;

public class DependencyGraph {

    List<Entry> queue;

    public DependencyGraph() {
        this.queue = new ArrayList<>();
    }

    public void queue(String name, Collection<String> dependencies) {
        this.queue.add(new Entry(name, new HashSet<>(dependencies), new HashSet<>()));
    }

    /**
     * Queues the name in the dependency graph. Evaluate this graph using {@link #finish()}.
     * For entries without dependencies, the load order returned by {@link #finish()} can be assumed to match
     * that of the calls to {@link #queue}.
     */
    public void queue(String name, Collection<String> dependencies, Collection<String> optionalDependencies) {
        this.queue.add(new Entry(name, new HashSet<>(dependencies), new HashSet<>(optionalDependencies)));
    }

    /**
     * Renders the dependency graph and returns a list of plugin names in the correct load order.
     *
     * @return List of plugins to load in that order.
     */
    public List<String> finish() {
        List<Entry> toLoad = new ArrayList<>();

        Map<String, Entry> map = new HashMap<>();

        // add reverse dependencies
        for (Entry e : this.queue) {
            e.dependencies.remove(e.name);

            if (map.put(e.name, e) != null) {
                throw new RuntimeException("Duplicate dependency graph item: " + e.name);
            }
            for (Entry e2 : this.queue) {
                if (e2.dependencies.contains(e.name)) {
                    e.dependents.add(e2.name);
                }
            }
        }

        // add transient dependencies
        for (Entry e : this.queue) {
            for (String dependency : new ArrayList<>(e.dependencies)) {
                Entry e2 = map.get(dependency);
                if (e2 != null) {
                    this.accumulateDependencies(map, e.dependencies, e2);
                }
            }
        }

        for (Entry e : this.queue) {
            int insertIndex = 0;
            for (; insertIndex < toLoad.size(); insertIndex++) {
                Entry other = toLoad.get(insertIndex);

                if (other.dependencies.contains(e.name)) {
                    // stop crawling up the list here.
                    break;
                }
            }

            toLoad.add(insertIndex, e);

            insertIndex++;
            for (; insertIndex < toLoad.size(); insertIndex++) {
                Entry other = toLoad.get(insertIndex);
                if (other.dependents.contains(e.name)) {
                    throw new RuntimeException("Cyclic dependency detected: " + e.name + " <-> " + other.name);
                }
            }
        }

        for (Entry e : toLoad) {
            for (String dependency : e.dependencies) {
                if (e.optional.contains(dependency)) {
                    // skip checking presence of optional deps
                    continue;
                }
                boolean anyMatches = false;
                for (Entry e2 : toLoad) {
                    if (e2.name.equals(dependency)) {
                        anyMatches = true;
                        break;
                    }
                }
                if (!anyMatches) {
                    throw new RuntimeException("\"" + e.name + "\" has unsatisfied dependency: \"" + dependency + "\"");
                }
            }
        }

        return toLoad.stream().map(e -> e.name).toList();
    }

    /**
     * Simulates ordered loading of plugins. Successful if no errors are thrown.
     */
    void test(List<String> finishOutput) {
        Map<String, Entry> map = new HashMap<>();
        for (Entry e : this.queue) {
            map.put(e.name, e);
        }

        List<String> loaded = new ArrayList<>();
        for (String plugin : finishOutput) {
            for (String dependency : map.get(plugin).dependencies) {
                Preconditions.checkState(loaded.contains(dependency), "Plugin '" + plugin + "' requires its dependency '" + dependency + "' to have been loaded first.");
            }
            loaded.add(plugin);
        }

        for (Entry required : this.queue) {
            Preconditions.checkState(loaded.contains(required.name), "Plugin '" + required.name + "' would not have been loaded");
        }
    }

    void accumulateDependencies(Map<String, Entry> map, HashSet<String> deps, Entry e) {
        for (String dependency : new ArrayList<>(e.dependencies)) {
            if (!deps.add(dependency)) {
                continue;
            }
            Entry e2 = map.get(dependency);
            if (e2 != null) {
                this.accumulateDependencies(map, deps, e);
            }
        }
    }

    class Entry {
        private String name;
        private HashSet<String> dependencies;
        private HashSet<String> dependents = new HashSet();
        private HashSet<String> optional;

        public Entry(String name, HashSet<String> dependencies, HashSet<String> optional) {
            this.name = name;
            this.dependencies = dependencies;
            this.optional = optional;
        }
    }
}
