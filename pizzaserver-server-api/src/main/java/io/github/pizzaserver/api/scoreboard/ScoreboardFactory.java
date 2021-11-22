package io.github.pizzaserver.api.scoreboard;

import java.util.function.Supplier;

public class ScoreboardFactory {

    private static Supplier<Scoreboard> constructor;


    public static Scoreboard create() {
        return constructor.get();
    }

    public static void setConstructor(Supplier<Scoreboard> scoreboardSupplier) {
        constructor = scoreboardSupplier;
    }

}
