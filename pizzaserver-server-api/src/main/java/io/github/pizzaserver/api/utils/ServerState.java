package io.github.pizzaserver.api.utils;

public enum ServerState {
    /**
     * The server is not booted up/no longer running.
     */
    INACTIVE,

    /**
     * The server is in the registration state.
     * Plugins should register all custom components at this point.
     */
    REGISTERING,

    /**
     * The server is currently enabling all plugins.
     */
    ENABLING_PLUGINS,

    /**
     * The server is booting up.
     */
    BOOT,

    /**
     * The server is running.
     */
    RUNNING,

    /**
     * The server is attempting to stop.
     */
    STOPPING
}
