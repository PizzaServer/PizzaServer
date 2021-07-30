package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.server.world.blocks.types.BaseBlockType;

public class Launcher {

    public static void main(String[] args) {
        ImplServer server = new ImplServer(System.getProperty("user.dir"));
        server.getBlockRegistry().register(new BaseBlockType() {
            @Override
            public String getBlockId() {
                return "craftedorigin:test_preset";
            }

            @Override
            public String getName() {
                return "test";
            }
        });
        server.boot();
    }

}
