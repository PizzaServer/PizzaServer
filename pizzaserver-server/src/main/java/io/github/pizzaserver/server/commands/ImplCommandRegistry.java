package io.github.pizzaserver.server.commands;

import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.commands.CommandRegistry;
import io.github.pizzaserver.api.commands.ImplCommand;
import io.github.pizzaserver.api.utils.ServerState;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.commands.defaults.AsyncTestingCommand;
import io.github.pizzaserver.server.commands.defaults.ExampleCommand;
import io.github.pizzaserver.server.commands.defaults.StopCommand;
import io.github.pizzaserver.api.commands.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImplCommandRegistry implements CommandRegistry {

    private volatile Map<String, Command> commands = new HashMap<>();
    private final Map<String, Command> aliases = new HashMap<>();

    private static Server server;
    private Thread consoleSender;
    private ThreadPoolExecutor asyncCommands = new ThreadPoolExecutor(0, 4, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));
    private volatile LinkedList<String> consoleCommandsQueue = new LinkedList<>();

    public ImplCommandRegistry(Server server) {
        ImplCommandRegistry.server = server;
        try {
            this.startConsoleCommandReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerDefaults() {
        server.getCommandRegistry().register(new ExampleCommand());
        server.getCommandRegistry().register(new StopCommand());
        server.getCommandRegistry().register(new AsyncTestingCommand());
    }

    @Override
    public void register(Command command) {
        this.register(command, command.getName());
    }

    @Override
    public void register(Command command, String label) {
        if (label == null)
            label = command.getName();
        label = label.trim().toLowerCase(Locale.ROOT);

        if (!commands.containsKey(label)) {
            commands.put(label, command);
            server.getLogger().info("Registered new command: " + label);
        } else {
            //TODO: Show the plugin name of the command that has been overwritten
            server.getLogger().warn("A command with the name `" + label + "` already exists from " + commands.get(label) + " when trying to register " + command + "!");
            return;
        }

        for(String alias : command.getAliases()) {
            alias = alias.trim().toLowerCase(Locale.ROOT);
            if(commands.containsKey(alias)) {
                server.getLogger().warn("A command with the name `" + alias + "` already exists from " + commands.get(alias) + " when trying to register " + command + "!");
                continue;
            }
            if(aliases.containsKey(alias)) {
                server.getLogger().warn("An alias with the name `" + alias + "` already exists from " + commands.get(alias) + " when trying to register " + command + "!");
                continue;
            }
            aliases.put(alias, command);
        }
    }

    @Override
    public void registerAll(List<Command> commands) {
        for(Command command : commands) {
            this.register(command, command.getName());
        }
    }

    @Override
    public void removeCommand(String name) {
        if(!commands.containsKey(name)) {
            throw new NullPointerException("That command doesn't exist");
        } else {
            commands.remove(name);
        }
    }

    @Override
    public Command getCommand(String name) {
        if(commands.containsKey(name)) return commands.get(name);
        if(aliases.containsKey(name)) return aliases.get(name);
        return null;
    }

    @Override
    public Map<String, Command> getCommands() {
        return new HashMap<>(commands);
    }

    @Override
    public AvailableCommandsPacket getAvailableCommands() {
        AvailableCommandsPacket availableCommandsPacket = new AvailableCommandsPacket();
        for(Command implCommand : commands.values()) {
            availableCommandsPacket.getCommands().add(implCommand.asCommandData());
        }
        return availableCommandsPacket;
    }

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void startConsoleCommandReader() throws IOException {
        this.consoleSender = new Thread(() -> {
            while(server.getState() != ServerState.STOPPING) {
                String readLine = null;
                try {
                    // Might have taken me three hours to find this (why does no one mention it, though the name explains it)
                    if(!reader.ready()) {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    readLine = reader.readLine().trim();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(readLine == null || readLine.equals(""))
                    continue;
                String[] list = readLine.split(" ");
                server.getLogger().warn("Read line: " + list[0]);
                Command realCommand = this.getCommand(list[0]);
                if(realCommand == null) {
                    server.getLogger().error("That command doesn't exist!");
                    continue;
                }
                try {
                    if(realCommand.isAsync()) {
                        this.runAsyncCommand(() -> realCommand.execute(null, Arrays.copyOfRange(list, 1, list.length), list[0]));
                    } else {
                        consoleCommandsQueue.push(readLine);
                    }
                } catch (Exception e) {
                    server.getLogger().error("Something went wrong executing that command!");
                    e.printStackTrace();
                }
            }
        });
        this.consoleSender.start();
    }

    @Override
    public void runAsyncCommand(Runnable runnable) {
        asyncCommands.execute(runnable);
    }

    @Override
    public void processConsoleCommands() {
        while(!consoleCommandsQueue.isEmpty()) {
            String[] commandLine = consoleCommandsQueue.poll().split(" ");
            if(commandLine[0].equals(""))
                continue;
            Command command = this.getCommand(commandLine[0]);
            if(command == null) {
                server.getLogger().error("That command doesn't exist!");
                continue;
            }
            try {
                command.execute(null, Arrays.copyOfRange(commandLine, 1, commandLine.length), commandLine[0]);
            } catch (Exception e) {
                server.getLogger().error("Something went wrong with that command!");
            }
        }
    }

    @Override
    public void shutdown() {
        asyncCommands.shutdown();
        try {
            reader.close();
            consoleSender.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
