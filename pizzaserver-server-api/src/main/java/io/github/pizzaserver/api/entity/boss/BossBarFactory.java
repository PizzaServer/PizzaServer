package io.github.pizzaserver.api.entity.boss;

import java.util.function.Supplier;

public class BossBarFactory {

    private static Supplier<BossBar> constructor;


    public static BossBar create() {
        return constructor.get();
    }

    public static void setConstructor(Supplier<BossBar> bossBarSupplier) {
        constructor = bossBarSupplier;
    }

}
