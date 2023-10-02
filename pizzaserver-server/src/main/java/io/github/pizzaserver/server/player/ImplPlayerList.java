package io.github.pizzaserver.server.player;

import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.PlayerList;

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
            this.sendPacket(PlayerListPacket.Action.ADD, Collections.singleton(entry));
        }
    }

    @Override
    public void addEntries(Collection<Entry> entries) {
        List<Entry> validEntries = entries.stream()
                .filter(entry -> !this.entries.contains(entry))
                .collect(Collectors.toList());
        if (validEntries.size() > 0) {
            this.entries.addAll(validEntries);
            this.sendPacket(PlayerListPacket.Action.ADD, validEntries);
        }
    }

    @Override
    public void removeEntry(Entry entry) {
        if (this.entries.contains(entry)) {
            this.entries.remove(entry);
            this.sendPacket(PlayerListPacket.Action.REMOVE, Collections.singleton(entry));
        }
    }

    @Override
    public void removeEntries(Collection<Entry> entries) {
        Set<Entry> validEntries = entries.stream()
                .filter(this.entries::contains)
                .collect(Collectors.toSet());
        if (validEntries.size() > 0) {
            this.entries.removeAll(validEntries);
            this.sendPacket(PlayerListPacket.Action.REMOVE, validEntries);
        }
    }

    @Override
    public List<Entry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    /**
     * Send a PlayerListPacket to this player.
     * @param action action type
     * @param entries entries to send
     */
    private void sendPacket(PlayerListPacket.Action action, Collection<Entry> entries) {
        PlayerListPacket addPlayerListPacket = new PlayerListPacket();
        addPlayerListPacket.setAction(action);
        addPlayerListPacket.getEntries().addAll(entries.stream().map(this::convertEntryToNetwork).collect(Collectors.toList()));
        this.player.sendPacket(addPlayerListPacket);
    }

    private PlayerListPacket.Entry convertEntryToNetwork(Entry entry) {
        PlayerListPacket.Entry networkEntry = new PlayerListPacket.Entry(entry.getUUID());
        networkEntry.setEntityId(entry.getEntityRuntimeId());
        networkEntry.setBuildPlatform(entry.getDevice().ordinal());
        networkEntry.setName(entry.getUsername());
        networkEntry.setSkin(entry.getSkin().serialize());
        networkEntry.setPlatformChatId(entry.getPlatformChatId());
        networkEntry.setXuid(entry.getXUID());
        networkEntry.setTrustedSkin(entry.getSkin().isTrusted());
        return networkEntry;
    }

}
