package io.github.pizzaserver.server.commands;

import com.nukkitx.protocol.bedrock.data.command.CommandParam;
import com.nukkitx.protocol.bedrock.data.command.CommandParamData;
import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.commands.Command;
import io.github.pizzaserver.api.commands.CommandRegistry;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.ServerState;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.commands.defaults.*;
import org.jline.console.CmdDesc;
import org.jline.reader.*;
import org.jline.widget.TailTipWidgets;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImplCommandRegistry extends SimpleTerminalConsole implements Completer, CommandRegistry {

    private final Server server;
    private final ImplServerSender sender;
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, Command> aliases = new HashMap<>();

    // Used to give helpful information about what commands layouts are/can be
    private final Map<String, CmdDesc> tailTips = new HashMap<>();

    private final ThreadPoolExecutor asyncCommands = new ThreadPoolExecutor(0, 4, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));


    public ImplCommandRegistry(Server server) {
        this.server = server;
        this.sender = new ImplServerSender(server);
        this.registerDefaults();
        // Start doesn't return until isRunning returns false or CTRL + C occurs
        // I'm not sure how to "safely" handle this, for now I'll just let it be
        // TODO: Start the console once all plugin commands are registered (once implemented)
        new Thread(() -> this.start(server)).start();
    }

    public void registerDefaults() {
        this.register(new ExampleCommand());
        this.register(new StopCommand());
        this.register(new GamemodeCommand(this));
        this.register(new AsyncTestingCommand());
        this.register(new Example2Command());
    }

    @Override
    public void register(Command command) {
        this.register(command, command.getName());
    }

    /**
     * @param command Command to register
     * @param label The main command name ({@link Command#getName()} if none is defined)
     */
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
        commands.remove(name);
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

    /**
     * Used to build the currently registered commands into a packet to be sent to the player
     * @return A packet filled with commands to be sent to the player
     */
    @Override
    public AvailableCommandsPacket getAvailableCommands() {
        AvailableCommandsPacket availableCommandsPacket = new AvailableCommandsPacket();
        for(Command command : commands.values()) {
            availableCommandsPacket.getCommands().add(command.asCommandData());
        }
        return availableCommandsPacket;
    }

    @Override
    protected void runCommand(String command) {
        if(server.getState() == ServerState.STOPPING) {
            // TODO: Add a forced command, kill, in case the server is taking too long to stop (say 30 seconds without ticking, also notify the player when the time is reached)
            server.getLogger().warn("The server is currently stopping and no longer accepting commands");
            return;
        }

        String[] list = command.split(" ");
        Command realCommand = this.getCommand(list[0]);
        if(realCommand == null) {
            server.getLogger().error("The command \"" + list[0] + "\" doesn't exist!");
            return;
        }
        if(realCommand.isAsync()) {
            this.runAsyncCommand(() -> {
                try {
                    realCommand.execute(this.sender, Arrays.copyOfRange(list, 1, list.length), list[0]);
                } catch (Exception e) {
                    server.getLogger().error("Error while executing " + realCommand.getName() + ": ", e);
                }
            });
        } else {
            try {
                realCommand.execute(this.sender, Arrays.copyOfRange(list, 1, list.length), list[0]);
            } catch (Exception e) {
                server.getLogger().error("Error while executing " + realCommand.getName() + ": ", e);
            }
        }
    }

    @Override
    public void runAsyncCommand(Runnable runnable) {
        asyncCommands.execute(runnable);
    }

    /**
     * This function is used to help make a convenient command line utility
     * If you want to mess around with console behavior (autocompletion, widgets, etc.),
     * here's the place! If you also want to do more complicated stuff, you can look
     * at JLine3. They have plenty of basic examples on how to do different things.
     * @param builder A base builder to configure
     * @return A built LineReader with widgets and autocompletion configure
     */
    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        builder.completer(this);
        builder.appName("PizzaServer");
        builder.option(LineReader.Option.DISABLE_EVENT_EXPANSION, true);
        builder.option(LineReader.Option.INSERT_TAB, false);
        builder.option(LineReader.Option.HISTORY_BEEP, false);
        builder.option(LineReader.Option.HISTORY_IGNORE_DUPS, true);
        builder.option(LineReader.Option.HISTORY_IGNORE_SPACE, true);
        builder.terminal(TerminalConsoleAppender.getTerminal());
        LineReader reader = builder.build();

        TailTipWidgets tailtipWidgets = new TailTipWidgets(reader, tailTips, 5, TailTipWidgets.TipType.TAIL_TIP);
        tailtipWidgets.enable();
        return reader;
    }

    /**
     *
     * @param lineReader        The line reader
     * @param parsedLine          The parsed command line
     * @param candidates    The {@link List} of candidates to populate
     */
    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> candidates) {
        if (parsedLine.wordIndex() == 0) {
            // The below is taken from Nukkit
            TreeSet<String> names = new TreeSet<>();
            names.addAll(this.commands.keySet());
            names.addAll(this.aliases.keySet());
            for (String match : names) {
                if (!match.toLowerCase().startsWith(parsedLine.word().toLowerCase())) {
                    continue;
                }
                candidates.add(new Candidate(match));
            }
        } else {
            Command command = commands.get(parsedLine.words().get(0));
            if(command == null)
                return;
            for(CommandParamData[] row : command.getOverloads()) {
                int position = 0;
                // TODO: Let parameters be specified with paths
                for(CommandParamData paramData : row) {
                    /* TODO: Predict the next word more accurately using command parameter types (if it's player, get a list of them)
                     *    Done is done or won't be done
                     *    Can't is can't be done (we can't predict an int/float parameter in console, maybe we can warn if it's above a limit with JLine?)
                     *    What is something I can't tell what it does
                     *    Maybe is something that is probably possible
                     *   Can't   INT
                     *   Can't   FLOAT
                     *   What?   VALUE
                     *   What?   WILDCARD_INT
                     *   What?   OPERATOR
                     *    Done   TARGET
                     *   What?   WILDCARD_TARGET
                     *  Maybe?   FILE_PATH          JLine has the FileNameCompleter
                     *   Can't   INT_RANGE
                     *    Done   STRING
                     *   Can't   POSITION
                     *   Can't   BLOCK_POSITION
                     *    Done   MESSAGE
                     *    Done   TEXT
                     *  Maybe?   JSON
                     *  Maybe?   COMMAND
                     *
                     * TODO: New idea for can'ts, we can have the autocomplete for parameters, though the parameters will *describe* the values
                     *   We can even have an added <INT> or <FLOAT> to the end of it
                     */
                    if (paramData.getType().equals(CommandParam.TARGET) && parsedLine.wordIndex()-1 == position) {
                        for(Player player : server.getPlayers()) {
                            String name = player.getName();
                            if(name.startsWith(parsedLine.word()) && parsedLine.wordIndex()-1 == position) {
                                candidates.add(new Candidate(name));
                            }
                        }
                    } else {
                        for(String value : paramData.getEnumData().getValues()) {
                            if(value.toLowerCase().startsWith(parsedLine.word().toLowerCase()) && parsedLine.wordIndex()-1 == position) {
                                candidates.add(new Candidate(value));
                            }
                        }
                    }
                    position++;
                }
            }
        }
    }

    /**
     * This is used to give some tips to the console as they type out a command
     * An example can be viewed at {@link GamemodeCommand} based on what I read
     * <a href="https://github.com/jline/jline3/wiki/Autosuggestions">here</a>
     * NOTE: This isn't related to autocompletion, it only shows information about what you provide
     * The completion is here under {@link ImplCommandRegistry#complete(LineReader, ParsedLine, List)}
     * @param name The command to be typed to start the tips (gamemode in the example)
     * @param cmdDesc A CmdDesc object filled with information about the completion
     */
    public void addTailTip(String name, CmdDesc cmdDesc) {
        this.tailTips.put(name, cmdDesc);
    }

    @Override
    public void shutdown() {
        // CTRL C was pressed
        ImplServer.getInstance().running = false;
        ImplServer.getInstance().state = ServerState.STOPPING;
        asyncCommands.shutdown();
    }

    @Override
    protected boolean isRunning() {
        return server.getState() != ServerState.STOPPING;
    }
}
