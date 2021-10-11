package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.data.ToolType;
import io.github.willqi.pizzaserver.api.item.types.BaseItemType;

public class Launcher {

    public static void main(String[] args) {
        ImplServer server = new ImplServer(System.getProperty("user.dir"));
        ItemRegistry.register(new BaseItemType() {
            @Override
            public String getItemId() {
                return "minecraft:wooden_pickaxe";
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public ToolType getToolType() {
                return ToolType.WOOD_PICKAXE;
            }

            @Override
            public String getIconName() {

                return null;
            }
        });
        server.boot();
    }

}
