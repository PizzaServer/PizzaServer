package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryAction;

import java.util.Collections;
import java.util.List;

public class ItemStackRequestPacket extends BaseBedrockPacket {

    public static final int ID = 0x93;

    private List<Request> requests = Collections.emptyList();


    public ItemStackRequestPacket() {
        super(ID);
    }

    public List<Request> getRequests() {
        return Collections.unmodifiableList(this.requests);
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }


    public static class Request {

        private final int id;
        private final List<InventoryAction> actions;
        private final List<String> customNames;

        public Request(int id, List<InventoryAction> actions) {
            this(id, actions, Collections.emptyList());
        }

        public Request(int id, List<InventoryAction> actions, List<String> customNames) {
            this.id = id;
            this.actions = actions;
            this.customNames = customNames;
        }

        public int getId() {
            return this.id;
        }

        public List<InventoryAction> getActions() {
            return this.actions;
        }

        public List<String> getCustomNames() {
            return this.customNames;
        }

    }

}
