package io.github.pizzaserver.server;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
        ImplServer server = new ImplServer(System.getProperty("user.dir"));
        VanillaContentLoader.load();
        server.boot();
    }

}
