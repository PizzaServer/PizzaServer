package io.github.willqi.pizzaserver.server;

public class Launcher {

    public static void main(String[] args) {
        Server server = new Server(System.getProperty("user.dir"));
        server.boot();
    }

}
