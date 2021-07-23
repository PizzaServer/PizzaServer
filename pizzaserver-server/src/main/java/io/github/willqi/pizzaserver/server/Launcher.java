package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.api.APIServer;

public class Launcher {

    public static void main(String[] args) {

        APIServer server = new Server(System.getProperty("user.dir"));
        server.boot();

    }

}
