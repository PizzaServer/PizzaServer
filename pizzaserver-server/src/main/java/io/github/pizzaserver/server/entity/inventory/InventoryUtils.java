package io.github.pizzaserver.server.entity.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;

import java.util.*;

public class InventoryUtils {

    private static final Map<ContainerType, Integer> containerSizes = new HashMap<>();
    private static final Map<ContainerType, Set<ContainerSlotType>> containerSlotTypes = new HashMap<>();

    private InventoryUtils() { }


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

    private static void register(ContainerType containerSlotType, int slots, ContainerSlotType[] slotTypes) {
        containerSizes.put(containerSlotType, slots);
        containerSlotTypes.put(containerSlotType, new HashSet<>(Arrays.asList(slotTypes)));
    }

    static {
        register(ContainerType.CONTAINER, 27, new ContainerSlotType[] {ContainerSlotType.CONTAINER});
        register(ContainerType.INVENTORY, 36, new ContainerSlotType[] {
                ContainerSlotType.ARMOR,
                ContainerSlotType.INVENTORY,
                ContainerSlotType.HOTBAR,
                ContainerSlotType.HOTBAR_AND_INVENTORY,
                ContainerSlotType.CURSOR,
                ContainerSlotType.OFFHAND
        });
    }
}
