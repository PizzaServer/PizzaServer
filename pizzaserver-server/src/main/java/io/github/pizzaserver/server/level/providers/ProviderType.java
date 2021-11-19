package io.github.pizzaserver.server.level.providers;

import io.github.pizzaserver.server.level.providers.leveldb.LevelDBLevelProvider;

import java.io.File;
import java.io.IOException;

public enum ProviderType {

    LEVELDB;


    public BaseLevelProvider create(File levelFile) throws IOException {
        switch (this) {
            case LEVELDB:
                return new LevelDBLevelProvider(levelFile);
            default:
                throw new NullPointerException("Unable to find ProviderType for this file");
        }
    }

    public static ProviderType resolveByFile(File fileDirectory) {
        // TODO: Implement a way to resolve provider types when implementing other provider types
        return LEVELDB;
    }

}
