package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.descriptors.ArmorItem;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class InventoryUtils {

    private static final Map<ContainerType, Integer> containerSizes = new HashMap<>();
    private static final Map<ContainerType, Set<ContainerSlotType>> containerSlotTypes = new HashMap<>();

    private InventoryUtils() {}


    /**
     * Retrieve the max slot count for a container.
     * @param containerType container type
     * @return slots
     */
    public static int getSlotCount(ContainerType containerType) {
        return containerSizes.getOrDefault(containerType, 0);
    }

    /**
     * Retrieve the slot types a container can have.
     * @param containerType container type
     * @return all container slot types
     */
    public static Set<ContainerSlotType> getSlotTypes(ContainerType containerType) {
        return containerSlotTypes.getOrDefault(containerType, Collections.emptySet());
    }

    public static boolean canBePlacedInSlot(Item item, ContainerSlotType containerSlotType, int slot) {
        return switch (containerSlotType) {
            case ARMOR -> item instanceof ArmorItem && slot == ((ArmorItem) item).getArmorSlot().ordinal();
            case OFFHAND -> item.isAllowedInOffHand();
            case FURNACE_FUEL -> item.getFuelTicks() != -1;
            case FURNACE_OUTPUT -> false;
            default -> true;
        };
    }

    private static void register(ContainerType containerSlotType, int slots, ContainerSlotType[] slotTypes) {
        containerSizes.put(containerSlotType, slots);
        containerSlotTypes.put(containerSlotType, new HashSet<>(Arrays.asList(slotTypes)));
    }

    static {
        register(ContainerType.CONTAINER, 27, new ContainerSlotType[] { ContainerSlotType.CONTAINER, ContainerSlotType.BARREL });
        register(ContainerType.DISPENSER, 9, new ContainerSlotType[] { ContainerSlotType.CONTAINER });
        register(ContainerType.DROPPER, 9, new ContainerSlotType[] { ContainerSlotType.CONTAINER });
        register(ContainerType.FURNACE, 3, new ContainerSlotType[] {
                ContainerSlotType.FURNACE_FUEL,
                ContainerSlotType.FURNACE_INGREDIENT,
                ContainerSlotType.FURNACE_OUTPUT
        });
        register(ContainerType.BLAST_FURNACE, 3, new ContainerSlotType[] {
                ContainerSlotType.FURNACE_FUEL,
                ContainerSlotType.BLAST_FURNACE_INGREDIENT,
                ContainerSlotType.FURNACE_OUTPUT
        });
        register(ContainerType.HOPPER, 5, new ContainerSlotType[] { ContainerSlotType.CONTAINER });
        register(ContainerType.INVENTORY,  36, new ContainerSlotType[] {
                ContainerSlotType.ARMOR,
                ContainerSlotType.INVENTORY,
                ContainerSlotType.HOTBAR,
                ContainerSlotType.HOTBAR_AND_INVENTORY,
                ContainerSlotType.CURSOR,
                ContainerSlotType.OFFHAND });
        register(ContainerType.WORKBENCH, 9, new ContainerSlotType[] {
                ContainerSlotType.CREATIVE_OUTPUT,
                ContainerSlotType.CRAFTING_INPUT
        });
        register(ContainerType.STONECUTTER, 1, new ContainerSlotType[] {
                ContainerSlotType.STONECUTTER_INPUT
        });
    }

}
