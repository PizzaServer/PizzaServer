package io.github.willqi.pizzaserver.server.world.providers;

import io.github.willqi.pizzaserver.server.world.providers.leveldb.LevelDBWorldProvider;

import java.io.File;
import java.io.IOException;

public enum ProviderType {

    LEVELDB;


    public BaseWorldProvider create(File worldFile) throws IOException {
        switch (this) {
            case LEVELDB:
                return new LevelDBWorldProvider(worldFile);
            default:
                throw new NullPointerException("Unable to find ProviderType for this file");
        }
    }

    public static ProviderType resolveByFile(File worldDirectory) {
        // TODO: Implement a way to resolve provider types when implementing other provider types
        return LEVELDB;
    }

}
