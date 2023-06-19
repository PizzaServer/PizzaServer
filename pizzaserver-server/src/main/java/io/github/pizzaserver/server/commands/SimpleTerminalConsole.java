package io.github.pizzaserver.server.commands;

import io.github.pizzaserver.api.Server;
import org.apache.logging.log4j.LogManager;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.impl.DumbTerminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class SimpleTerminalConsole {

    /**
     * Determines if the application is still running and accepting input.
     *
     * @return {@code true} to continue reading input
     */
    protected abstract boolean isRunning();

    /**
     * Run a command entered in the console.
     *
     * @param command The command line to run
     */
    protected abstract void runCommand(String command);

    /**
     * Shutdown the application and perform a clean exit.
     *
     * <p>This is called if the application receives SIGINT while reading input,
     * e.g. when pressing CTRL+C on most terminal implementations.</p>
     */
    protected abstract void shutdown();

    /**
     * Process an input line entered through the console.
     *
     * <p>The default implementation trims leading and trailing whitespace
     * from the input and skips execution if the command is empty.</p>
     *
     * @param input The input line
     */
    protected void processInput(String input) {
        String command = input.trim();
        if (!command.isEmpty()) {
            runCommand(command);
        }
    }

    /**
     * Configures the {@link LineReaderBuilder} and {@link LineReader} with
     * additional options.
     *
     * <p>Override this method to make further changes, (e.g. call
     * {@link LineReaderBuilder#appName(String)} or
     * {@link LineReaderBuilder#completer(Completer)}).</p>
     *
     * <p>The default implementation sets some opinionated default options,
     * which are considered to be appropriate for most applications:</p>
     *
     * <ul>
     *     <li>{@link LineReader.Option#DISABLE_EVENT_EXPANSION}: JLine implements
     *     <a href="http://www.gnu.org/software/bash/manual/html_node/Event-Designators.html">
     *     Bash's Event Designators</a> by default. These usually do not
     *     behave as expected in a simple command environment, so it's
     *     recommended to disable it.</li>
     *     <li>{@link LineReader.Option#INSERT_TAB}: By default, JLine inserts
     *     a tab character when attempting to tab-complete on empty input.
     *     It is more intuitive to show a list of commands instead.</li>
     * </ul>
     *
     * @param builder The builder to configure
     * @return The built line reader
     */
    protected LineReader buildReader(LineReaderBuilder builder) {
        LineReader reader = builder.build();
        reader.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION);
        reader.unsetOpt(LineReader.Option.INSERT_TAB);
        return reader;
    }

    /**
     * Start reading commands from the console.
     *
     * <p>Note that this method won't return until one of the following
     * conditions are met:</p>
     *
     * <ul>
     *     <li>{@link #isRunning()} returns {@code false}, indicating that the
     *     application is shutting down.</li>
     *     <li>{@link #shutdown()} is triggered by the user (e.g. due to
     *     pressing CTRL+C)</li>
     *     <li>The input stream is closed.</li>
     * </ul>
     */
    public void start(Server server) {
        try {
            TerminalConsoleAppender.newBuilder().setName("Pizza Server").build();
            final @Nullable Terminal terminal = TerminalConsoleAppender.getTerminal();
            if (terminal instanceof DumbTerminal) {
                server.getLogger().warn("You're running this in a dumb terminal (most likely an IDE). Some command features may not work as intended!");
                readCommands(System.in);
            } else {
                readCommands(terminal);
            }
        } catch (IOException e) {
            LogManager.getLogger("TerminalConsole").error("Failed to read console input", e);
        }
    }

    private void readCommands(Terminal terminal) {
        LineReader reader = buildReader(LineReaderBuilder.builder().terminal(terminal));
        TerminalConsoleAppender.setReader(reader);

        try {
            String line;
            while (isRunning()) {
                try {
                    line = reader.readLine("> ");
                } catch (EndOfFileException ignored) {
                    // Continue reading after EOT
                    continue;
                }

                if (line == null) {
                    break;
                }

                processInput(line);
            }
        } catch (UserInterruptException e) {
            shutdown();
        } finally {
            TerminalConsoleAppender.setReader(null);
        }
    }

    private void readCommands(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while (isRunning() && (line = reader.readLine()) != null) {
                processInput(line);
            }
        }
    }

}
