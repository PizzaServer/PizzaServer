package io.github.pizzaserver.server.commands.defaults;

import com.nukkitx.protocol.bedrock.data.command.CommandEnumData;
import com.nukkitx.protocol.bedrock.data.command.CommandParam;
import com.nukkitx.protocol.bedrock.data.command.CommandParamData;
import io.github.pizzaserver.api.commands.CommandSender;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.server.commands.ImplCommand;

public class GamemodeCommand extends ImplCommand {

    public GamemodeCommand() {
        super("gamemode");
        overloads = new CommandParamData[2][2];
        registerParameter(0, 0, "gamemode", new CommandEnumData("gmode", new String[]{"creative", "survival"}, true), CommandParam.TEXT);
        registerParameter(0, 1, "gamemode", new String[]{}, CommandParam.TARGET);
        registerParameter(1, 1, "testing", new String[]{"AAA", "AAA2"});
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
