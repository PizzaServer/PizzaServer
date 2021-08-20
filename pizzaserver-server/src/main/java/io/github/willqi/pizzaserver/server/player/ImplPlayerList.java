package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerListPacket;

import java.util.*;
import java.util.stream.Collectors;

public class ImplPlayerList implements PlayerList {

    private final Player player;
    private final Set<Entry> entries = new LinkedHashSet<>();


    public ImplPlayerList(Player player) {
        this.player = player;
    }

    @Override
    public void addEntry(Entry entry) {
        if (!this.entries.contains(entry)) {
            this.entries.add(entry);
            this.sendPacket(PlayerListPacket.ActionType.ADD, Collections.singleton(entry));
        }
    }

    @Override
    public void addEntries(Collection<Entry> entries) {
        List<Entry> validEntries = entries.stream()
                .filter(entry -> !this.entries.contains(entry))
                .collect(Collectors.toList());
        if (validEntries.size() > 0) {
            this.entries.addAll(validEntries);
            this.sendPacket(PlayerListPacket.ActionType.ADD, validEntries);
        }
    }

    @Override
    public void removeEntry(Entry entry) {
        if (this.entries.contains(entry)) {
            this.entries.remove(entry);
            this.sendPacket(PlayerListPacket.ActionType.REMOVE, Collections.singleton(entry));
        }
    }

    @Override
    public void removeEntries(Collection<Entry> entries) {
        Set<Entry> validEntries = entries.stream()
                .filter(this.entries::contains)
                .collect(Collectors.toSet());
        if (validEntries.size() > 0) {
            this.entries.removeAll(validEntries);
            this.sendPacket(PlayerListPacket.ActionType.REMOVE, validEntries);
        }
    }

    @Override
    public List<Entry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    /**
     * Send a PlayerListPacket to this player
     * @param actionType action type
     * @param entries entries to send
     */
    private void sendPacket(PlayerListPacket.ActionType actionType, Collection<Entry> entries) {
        PlayerListPacket addPlayerListPacket = new PlayerListPacket();
        addPlayerListPacket.setActionType(actionType);
        addPlayerListPacket.setEntries(entries);
        this.player.sendPacket(addPlayerListPacket);
    }

}
