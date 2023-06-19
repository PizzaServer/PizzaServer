package io.github.pizzaserver.server.commands.defaults;

import com.nukkitx.protocol.bedrock.data.command.CommandParam;
import com.nukkitx.protocol.bedrock.data.command.CommandParamData;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.commands.CommandRegistry;
import io.github.pizzaserver.api.commands.CommandSender;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.commands.ImplCommand;
import io.github.pizzaserver.server.commands.ImplCommandRegistry;
import org.jline.console.ArgDesc;
import org.jline.console.CmdDesc;
import org.jline.utils.AttributedString;

import java.util.*;

public class GamemodeCommand extends ImplCommand {

    public GamemodeCommand(CommandRegistry registry) {
        super("gamemode");
        overloads = new CommandParamData[1][2];
        registerParameter(0, 0, "gamemode", new String[]{"creative", "adventure", "spectator", "survival"});
        registerParameter(0, 1, "player", new String[]{}, CommandParam.TARGET);
        // TODO: Find a cleaner way of doing this, goal is to have a simple way to register tail tips
        if(registry instanceof ImplCommandRegistry) {
            this.registerTailTip((ImplCommandRegistry) registry, new String[]{
                    "gamemode creative [target]",
                    "gmaemode survival [target]",
                    "gamemode spectator [target] - Not fully implemented yet",
                    "gamemode adventure [target]"
            });
        }
    }

    public void registerTailTip(ImplCommandRegistry registry, String[] lines) {
        //Map<String, List<AttributedString>> widgetOpts = new HashMap<>();
        // SO We autocomplete in the area above while giving a list underneath, so combined or normal?
        List<AttributedString> mainDescriptions = new ArrayList<>();
        for(String line : lines) {
            mainDescriptions.add(new AttributedString(line));
            // This is the - "" informational part, though I can't get it to work without the `-`
            // and for more than 1 character
            //widgetOpts.put("-spectator", Arrays.asList(new AttributedString("more information")));
        }
        registry.addTailTip(this.getName(), new CmdDesc(
                mainDescriptions,
                ArgDesc.doArgNames(Arrays.asList("[gamemode...]", "[target...]", "")),
                new HashMap<>()
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if(!(sender instanceof Player player)) {
            if(args.length == 0 || args.length == 1) {
                sender.sendError("Please identify a gamemode and player!");
                return;
            }
            String toFind = args[1].trim();
            Player target = null;
            for(Player possiblity : sender.getServer().getPlayers()) {
                if(possiblity.getName().equals(toFind)) {
                    target = possiblity;
                    break;
                }
            }
            if(target == null) {
                sender.sendError("That player doesn't exist!");
                return;
            }
            switch(args[0].toLowerCase().trim()) {
                case "survival", "0" -> {target.setGamemode(Gamemode.SURVIVAL);}
                case "creative", "1" -> {target.setGamemode(Gamemode.CREATIVE);}
                case "spectator", "3" -> {target.setGamemode(Gamemode.SPECTATOR);}
                case "adventure", "2" -> {target.setGamemode(Gamemode.ADVENTURE);}
                default -> {sender.sendError("That isn't a valid gamemode!");}
            }
            return;
        }
        //TODO: Permissions
        if(args.length == 0) {
            player.sendError("Please identify a gamemode!");
            return;
        }
        switch(args[0].toLowerCase().trim()) {
            case "survival", "0" -> {player.setGamemode(Gamemode.SURVIVAL);}
            case "creative", "1" -> {player.setGamemode(Gamemode.CREATIVE);}
            case "spectator", "3" -> {player.setGamemode(Gamemode.SPECTATOR);}
            case "adventure", "2" -> {player.setGamemode(Gamemode.ADVENTURE);}
            default -> {player.sendError("That isn't a valid gamemode!");}
        }
    }
}
