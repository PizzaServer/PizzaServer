package io.github.willqi.pizzaserver.server;

public class Launcher {

    public static void main(String[] args) {
        ImplServer server = new ImplServer(System.getProperty("user.dir"));
        server.boot();
    }

}
