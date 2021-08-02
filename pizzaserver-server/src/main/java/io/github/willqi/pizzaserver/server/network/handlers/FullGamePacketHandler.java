package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.commands.ImplCommand;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.event.type.player.PlayerChatEvent;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;
import io.netty.util.internal.EmptyArrays;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FullGamePacketHandler extends BaseBedrockPacketHandler {

    private final ImplPlayer player;


    public FullGamePacketHandler(ImplPlayer player) {
        this.player = player;
        this.completeLogin();
    }

    /**
     * Send all remaining packets required before the player can see the world
     */
    private void completeLogin() {
        String defaultWorldName = this.player.getServer().getConfig().getDefaultWorldName();
        World defaultWorld = this.player.getServer().getWorldManager().getWorld(defaultWorldName);
        if (defaultWorld == null) {
            this.player.disconnect("Failed to find default world");
            ImplServer.getInstance().getLogger().error("Failed to find a world by the name of " + defaultWorldName);
            return;
        }

        // TODO: get actual player spawn from player data
        Vector3 playerSpawn = new Vector3(142, 66, 115);

        // Load the chunks around the player before we spawn them in
        int playerChunkX = playerSpawn.toVector3i().getX() / 16;
        int playerChunkZ = playerSpawn.toVector3i().getZ() / 16;

        Set<CompletableFuture<Chunk>> chunkTasksRequired = new HashSet<>();
        for (int chunkX = playerChunkX - this.player.getChunkRadius(); chunkX <= playerChunkX + this.player.getChunkRadius(); chunkX++) {
            for (int chunkZ = playerChunkZ - this.player.getChunkRadius(); chunkZ <= playerChunkZ + this.player.getChunkRadius(); chunkZ++) {
                chunkTasksRequired.add(defaultWorld.getChunkManager().fetchChunk(chunkX, chunkZ));
            }
        }
        CompletableFuture.runAsync(() -> {
            for (CompletableFuture<Chunk> chunkTask : chunkTasksRequired) {
                chunkTask.join();
            }
        }).whenComplete((ignored, exception) -> {
            if (exception != null) {
                this.player.disconnect("Failed to load chunks around player");
                return;
            }
            defaultWorld.addEntity(this.player, playerSpawn);
        });
    }

    @Override
    public void onPacket(RequestChunkRadiusPacket packet) {
        this.player.setChunkRadiusRequested(packet.getChunkRadiusRequested());

        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(this.player.getChunkRadius());
        this.player.sendPacket(chunkRadiusUpdatedPacket);
    }

    @Override
    public void onPacket(MovePlayerPacket packet) {
        ImplLocation newLocation = new ImplLocation(this.player.getLocation().getWorld(), packet.getPosition());
        this.player.setLocation(newLocation);
    }

    @Override
    public void onPacket(TextPacket packet) {
        if (packet.getType() == TextPacket.TextType.CHAT) {
            PlayerChatEvent event = new PlayerChatEvent(this.player, packet.getMessage(), this.player.getServer().getPlayers());

            this.player.getServer().getEventManager().call(event);
            if (!event.isCancelled()) {
                for (Player recipient : event.getRecipients()) {
                    recipient.sendPlayerMessage(this.player, event.getMessage());
                }
            }

        }
    }

    @Override
    public void onPacket(WorldSoundEventPacket packet) {
        WorldSoundEvent event = new WorldSoundEvent(player.getLocation().getWorld(), packet);
        this.player.getServer().getEventManager().call(event);
        if(!event.isCancelled()) {
            event.getWorld().playSound(packet.getSound(), packet.getVector3(), packet.isGlobal(), packet.isBaby(), packet.getEntityType(), packet.getBlockID());
            for(Player player : event.getWorld().getPlayers()) {
                if(player.getUUID() != this.player.getUUID()) player.sendPacket(packet);
            }
        }
    }

    @Override
    public void onPacket(CommandRequestPacket packet) {
        ArrayList<String> commandArgs = parseArguments(packet.getCommand());
        String label = commandArgs.remove(0).toLowerCase(Locale.ROOT).substring(1);
        String[] args = commandArgs.toArray(EmptyArrays.EMPTY_STRINGS);

        ImplCommand command = player.getServer().getCommandMap().getCommand(label);
        if(command == null) {
            player.sendMessage("That command doesn't exist!");
            return;
        }
        command.execute(player, args, label);
    }

    private ArrayList<String> parseArguments(String cmdLine) {
        StringBuilder sb = new StringBuilder(cmdLine);
        ArrayList<String> args = new ArrayList<>();
        boolean notQuoted = true;
        int start = 0;

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '\\') {
                sb.deleteCharAt(i);
                continue;
            }

            if (sb.charAt(i) == ' ' && notQuoted) {
                String arg = sb.substring(start, i);
                if (!arg.isEmpty()) {
                    args.add(arg);
                }
                start = i + 1;
            } else if (sb.charAt(i) == '"') {
                sb.deleteCharAt(i);
                --i;
                notQuoted = !notQuoted;
            }
        }

        String arg = sb.substring(start);
        if (!arg.isEmpty()) {
            args.add(arg);
        }
        return args;
    }
}
