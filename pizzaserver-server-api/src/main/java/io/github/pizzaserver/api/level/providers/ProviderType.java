package io.github.pizzaserver.api.level.providers;

import java.io.File;

public enum ProviderType {

    LEVELDB;


    public static ProviderType resolveByFile(File file) {
        // TODO: Implement a way to resolve provider types when implementing other provider types
        return LEVELDB;
    }
}
